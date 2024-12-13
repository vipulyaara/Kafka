package com.kafka.kms.data.remote

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.SecretsProvider
import com.kafka.base.debug
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.fileFormatAudio
import com.kafka.data.model.MediaType
import com.kafka.kms.ui.upload.ContentOpfParser
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Count
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import org.w3c.dom.Element
import org.w3c.dom.NodeList
import java.io.IOException
import java.util.UUID
import javax.xml.parsers.DocumentBuilderFactory
import java.io.File as JavaFile

@Inject
class SupabaseUploadService(
    private val secretsProvider: SecretsProvider,
    private val supabaseClient: SupabaseClient,
    private val httpClient: HttpClient,
    private val dispatchers: CoroutineDispatchers
) {
    suspend fun fetchItem(itemId: String): Result<Pair<Item, ItemDetail>> = runCatching {
        withContext(Dispatchers.IO) {
            val item = supabaseClient.from("items")
                .select { filter { eq("item_id", itemId) } }
                .decodeSingle<Item>()

            val itemDetail = supabaseClient.from("item_detail")
                .select { filter { eq("item_id", itemId) } }
                .decodeSingle<ItemDetail>()

            item to itemDetail
        }
    }

    suspend fun uploadBook(
        itemId: String,
        title: String,
        description: String,
        longDescription: String,
        creators: String,
        subjects: String,
        publishers: String,
        languages: String,
        collections: String,
        translators: String,
        coverImagePaths: List<String>,
        epubFilePath: String,
        contentOpfPath: String,
        mediaType: MediaType,
        copyrightText: String,
        copyrighted: Boolean?,
        isUpdate: Boolean
    ): Result<Unit> = runCatching {
        // Handle files based on media type
        val (coverUrls, _) = when {
            mediaType.isAudio -> {
                // For audio, we only upload cover images
                val coverUrls = uploadFiles(itemId, coverImagePaths)
                coverUrls to null
            }
            !isUpdate || epubFilePath.isNotEmpty() -> {
                // For new books or updates with new epub
                uploadFiles(itemId, coverImagePaths, epubFilePath)
            }
            else -> {
                // For updates without new epub, only handle cover images
                val coverUrls = uploadFiles(itemId, coverImagePaths)
                coverUrls to null
            }
        }

        // Convert comma-separated strings to lists
        val creatorsList = creators.split(",").map { it.trim() }
        val subjectsList = subjects.split(",").map { it.trim() }
        val publishersList = publishers.split(",").map { it.trim() }
        val languagesList = languages.split(",").map { it.trim() }
        val collectionsList = collections.split(",").map { it.trim() }
        val translatorsList = translators.split(",").map { it.trim() }

        // Create Item entity
        val item = Item(
            itemId = itemId,
            title = title,
            description = description,
            creators = creatorsList,
            subjects = subjectsList,
            languages = languagesList,
            collections = collectionsList,
            coverImage = coverUrls.firstOrNull(),
            mediaType = mediaType
        )

        // Create ItemDetail entity
        val itemDetail = ItemDetail(
            itemId = itemId,
            title = title,
            mediaType = mediaType,
            description = longDescription,
            creators = creatorsList,
            translators = translatorsList,
            collections = collectionsList,
            languages = languagesList,
            coverImages = coverUrls,
            subjects = subjectsList,
            publishers = publishersList,
            copyright = copyrighted,
            copyrightText = copyrightText
        )

        // Upload to Supabase
        withContext(dispatchers.io) {
            // Check if item exists
            val itemCount = supabaseClient.from("items").select {
                    filter { Item::itemId eq itemId }
                    count(Count.EXACT)
                }.countOrNull()

            val itemExists = (itemCount ?: 0) > 0

            if (itemExists) {
                // Update existing records
                supabaseClient.from("items")
                    .update(item) { filter { eq("item_id", itemId) } }

                supabaseClient.from("item_detail")
                    .update(itemDetail) { filter { eq("item_id", itemId) } }
            } else {
                // Insert new records
                supabaseClient.from("items").insert(item)
                supabaseClient.from("item_detail").insert(itemDetail)
            }

            // Handle audio sections if it's an audio book
            if (mediaType.isAudio && contentOpfPath.isNotEmpty()) {
                val opfMetadata = ContentOpfParser.parse(contentOpfPath)

                debug { "OPF metadata is $opfMetadata" }

                // Parse audio sections
                val document = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder()
                    .parse(JavaFile(contentOpfPath))

                val manifest = document.documentElement
                    .getElementsByTagName("manifest")
                    .item(0) as Element

                val audioItems = manifest.getElementsByTagName("item")
                    .toElementList()
                    .filter { it.getAttribute("media-type") == "audio/mpeg" }

                audioItems.forEach { audioItem ->
                    val sectionId = audioItem.getAttribute("id")
                    val fileId = "${itemId}_$sectionId"

                    // Check if file exists
                    val fileCount  = supabaseClient.from("files").select {
                        filter { File::fileId eq fileId }
                        count(Count.EXACT)
                    }.countOrNull()

                    val fileExists = (fileCount ?: 0) > 0

                    val url = audioItem.getAttribute("href")
                    val duration = audioItem.getAttribute("duration")
                    val fileTitle = audioItem.getElementsByTagName("meta")
                        .toElementList()
                        .find { it.getAttribute("property") == "title" }
                        ?.textContent ?: sectionId

                    val position = audioItem.getElementsByTagName("meta")
                        .toElementList()
                        .find { it.getAttribute("property") == "position" }
                        ?.textContent?.toIntOrNull() ?: 0

                    val readers = audioItem.getElementsByTagName("meta")
                        .toElementList()
                        .find { it.getAttribute("property") == "readers" }
                        ?.textContent?.split(",")?.map { it.trim() }
                        ?: opfMetadata.creators.takeIf { it.isNotEmpty() }
                        ?: creatorsList

                    val language = audioItem.getElementsByTagName("meta")
                        .toElementList()
                        .find { it.getAttribute("property") == "language" }
                        ?.textContent ?: languagesList.firstOrNull() ?: "en"

                    debug { "File duration is $duration" }

                    val file = File(
                        fileId = fileId,
                        itemId = itemId,
                        itemTitle = title,
                        title = fileTitle,
                        mediaType = MediaType.Audio,
                        extension = "mp3",
                        format = fileFormatAudio,
                        url = url,
                        coverImage = coverUrls.firstOrNull(),
                        creators = readers,
                        position = position,
                        languages = listOf(language),
                        duration = duration.toLongOrNull()
                    )

                    if (fileExists) {
                        supabaseClient.from("files")
                            .update(file) { filter { eq("file_id", fileId) } }
                    } else {
                        supabaseClient.from("files").insert(file)
                    }
                }
            }
        }
    }

    private suspend fun uploadFiles(
        itemId: String,
        coverImagePaths: List<String>
    ): List<String> = withContext(Dispatchers.IO) {
        try {
            debug { "Starting cover image uploads for item: $itemId" }

            // Handle cover images - only upload new local files
            val coverUrls = coverImagePaths.map { path ->
                if (path.startsWith("http")) {
                    // Keep existing remote URLs as-is
                    path
                } else {
                    // Upload new local files
                    val coverFile = JavaFile(path)
                    val coverFileName = "covers/$itemId/${UUID.randomUUID()}.${coverFile.extension}"
                    uploadFile(
                        bucket = "items",
                        objectName = coverFileName,
                        file = coverFile,
                        contentType = "image/${coverFile.extension}"
                    )
                }
            }
            debug { "Cover uploads successful" }

            coverUrls
        } catch (e: Exception) {
            debug { "Upload error: ${e.message}" }
            throw e
        }
    }

    private suspend fun uploadFiles(
        itemId: String,
        coverImagePaths: List<String>,
        epubFilePath: String
    ): Pair<List<String>, String> = withContext(Dispatchers.IO) {
        try {
            // Upload cover images first
            val coverUrls = uploadFiles(itemId, coverImagePaths)

            // Upload epub
            debug { "Uploading EPUB file..." }
            val epubFile = JavaFile(epubFilePath)
            val epubFileName = "readables/$itemId/${UUID.randomUUID()}.${epubFile.extension}"
            val epubUrl = uploadFile(
                bucket = "items",
                objectName = epubFileName,
                file = epubFile,
                contentType = "application/epub+zip"
            )
            debug { "EPUB upload successful" }

            coverUrls to epubUrl
        } catch (e: Exception) {
            debug { "Upload error: ${e.message}" }
            throw e
        }
    }

    private suspend fun uploadFile(
        bucket: String,
        objectName: String,
        file: JavaFile,
        contentType: String
    ): String {
        if (!file.exists()) {
            throw IllegalArgumentException("File does not exist: ${file.path}")
        }

        val supabaseUrl = secretsProvider.supabaseUrl
        val supabaseKey = secretsProvider.supabaseAdminKey
        val uploadUrl = "$supabaseUrl/storage/v1/object/$bucket/$objectName"

        debug { "Uploading file: $objectName (${file.length()} bytes)" }

        try {
            val fileBytes = file.readBytes()
            val response = httpClient.post(uploadUrl) {
                headers {
                    append("apikey", supabaseKey)
                    append("Authorization", "Bearer $supabaseKey")
                    append(HttpHeaders.ContentType, contentType)
                    append(HttpHeaders.ContentLength, file.length().toString())
                }
                setBody(fileBytes)
            }

            if (!response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                debug { "File Upload failed with response: $responseBody" }
                throw IOException("File Upload failed: ${response.status}")
            }

            // Return the public URL
            return "$supabaseUrl/storage/v1/object/public/$bucket/$objectName"
        } catch (e: Exception) {
            debug { "Upload error for $objectName: ${e.message}" }
            throw e
        }
    }

    private val JavaFile.extension: String
        get() = name.substringAfterLast('.', "")

    private fun NodeList.toElementList(): List<Element> =
        (0 until length).map { item(it) as Element }
} 
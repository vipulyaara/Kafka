package com.kafka.kms.service

import com.kafka.base.SecretsProvider
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
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
import java.io.IOException
import java.util.UUID
import java.io.File as JavaFile

@Inject
class SupabaseUploadService(
    private val secretsProvider: SecretsProvider,
    private val supabaseClient: SupabaseClient,
    private val httpClient: HttpClient
) {
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
        coverFilePath: String,
        epubFilePath: String,
        mediaType: MediaType,
        copyright: String
    ): Result<String> = runCatching {
        val (coverUrl, epubUrl) = uploadFiles(itemId, coverFilePath, epubFilePath)

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
            mediaType = mediaType,
            creators = creatorsList,
            languages = languagesList,
            description = description,
            coverImage = coverUrl,
            collections = collectionsList,
            subjects = subjectsList
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
            coverImage = coverUrl,
            subjects = subjectsList,
            publishers = publishersList
        )

        // Create File entity
        val file = File(
            fileId = UUID.randomUUID().toString(),
            itemId = itemId,
            itemTitle = title,
            title = title,
            mediaType = mediaType,
            coverImage = coverUrl,
            extension = "epub",
            creators = creatorsList,
            duration = null,
            format = "application/epub+zip",
            url = epubUrl,
            size = JavaFile(epubFilePath).length()
        )

        withContext(Dispatchers.IO) {
            // Insert into Supabase database
            supabaseClient.postgrest["items"].insert(item)
            supabaseClient.postgrest["item_detail"].insert(itemDetail)
            supabaseClient.postgrest["files"].insert(file)
        }

        itemId
    }

    private suspend fun uploadFiles(
        itemId: String,
        coverFilePath: String,
        epubFilePath: String
    ): Pair<String, String> = withContext(Dispatchers.IO) {
        try {
            println("Starting file uploads for item: $itemId")
            println("Cover file path: $coverFilePath")
            println("EPUB file path: $epubFilePath")

            val coverFile = JavaFile(coverFilePath)
            val epubFile = JavaFile(epubFilePath)

            println("File sizes:")
            println("- Cover: ${coverFile.length()} bytes")
            println("- EPUB: ${epubFile.length()} bytes")

            val coverFileName = "covers/$itemId/${UUID.randomUUID()}.${coverFile.extension}"
            val epubFileName = "readables/$itemId/${UUID.randomUUID()}.${epubFile.extension}"

            println("Generated file names:")
            println("- Cover: $coverFileName")
            println("- EPUB: $epubFileName")

            // Upload cover
            println("Uploading cover file...")
            val coverUrl = uploadFile(
                bucket = "items",
                objectName = coverFileName,
                file = coverFile,
                contentType = "image/${coverFile.extension}"
            )
            println("Cover upload successful: $coverUrl")

            // Upload epub
            println("Uploading EPUB file...")
            val epubUrl = uploadFile(
                bucket = "items",
                objectName = epubFileName,
                file = epubFile,
                contentType = "application/epub+zip"
            )
            println("EPUB upload successful: $epubUrl")

            coverUrl to epubUrl
        } catch (e: Exception) {
            println("Upload error in uploadFiles:")
            println("- Exception type: ${e.javaClass.name}")
            println("- Message: ${e.message}")
            e.printStackTrace()
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
        val supabaseKey = secretsProvider.supabaseKey
        val uploadUrl = "$supabaseUrl/storage/v1/object/$bucket/$objectName"

        println("Attempting file upload:")
        println("- Bucket: $bucket")
        println("- Object name: $objectName")
        println("- File size: ${file.length()} bytes")
        println("- Content type: $contentType")
        println("- Upload URL: $uploadUrl")

        try {
            val fileBytes = file.readBytes()
            println("- File bytes read successfully: ${fileBytes.size} bytes")

            val response = httpClient.post(uploadUrl) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer $supabaseKey")
                    append(HttpHeaders.ContentType, contentType)
                    append(HttpHeaders.ContentLength, file.length().toString())
                }
                setBody(fileBytes)
            }

            println("Upload response:")
            println("- Status: ${response.status}")
            println("- Status code: ${response.status.value}")

            if (!response.status.isSuccess()) {
                val responseBody = response.bodyAsText()
                println("- Error response body: $responseBody")
                throw IOException("""
                    Upload failed:
                    Status: ${response.status}
                    Body: $responseBody
                    URL: $uploadUrl
                    Content-Type: $contentType
                    File size: ${file.length()}
                """.trimIndent())
            }

            // Return the public URL
            return "$supabaseUrl/storage/v1/object/public/$bucket/$objectName"
        } catch (e: Exception) {
            println("Exception during upload:")
            println("- Exception type: ${e.javaClass.name}")
            println("- Message: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    private val JavaFile.extension: String
        get() = name.substringAfterLast('.', "")
} 
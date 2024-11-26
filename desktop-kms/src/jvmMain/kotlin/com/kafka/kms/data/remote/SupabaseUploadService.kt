package com.kafka.kms.data.remote

import com.kafka.base.SecretsProvider
import com.kafka.base.debug
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
        coverImagePaths: List<String>,
        epubFilePath: String,
        mediaType: MediaType,
        copyright: String
    ): Result<Unit> = runCatching {
        val (coverUrls, epubUrl) = uploadFiles(itemId, coverImagePaths, epubFilePath)

        // Convert comma-separated strings to lists
        val creatorsList = creators.split(",").map { it.trim() }
        val subjectsList = subjects.split(",").map { it.trim() }
        val publishersList = publishers.split(",").map { it.trim() }
        val languagesList = languages.split(",").map { it.trim() }
        val collectionsList = collections.split(",").map { it.trim() }
        val translatorsList = translators.split(",").map { it.trim() }

        // Create Item entity (using first cover image as primary)
        val item = Item(
            itemId = itemId,
            title = title,
            mediaType = mediaType,
            creators = creatorsList,
            languages = languagesList,
            description = description,
            coverImage = coverUrls.firstOrNull() ?: "",
            collections = collectionsList,
            subjects = subjectsList
        )

        // Create ItemDetail entity with all cover images
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
            publishers = publishersList
        )

        // Create File entity
        val file = File(
            fileId = UUID.randomUUID().toString(),
            itemId = itemId,
            itemTitle = title,
            title = title,
            mediaType = mediaType,
            coverImage = coverUrls.firstOrNull() ?: "",
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

        Unit
    }

    private suspend fun uploadFiles(
        itemId: String,
        coverImagePaths: List<String>,
        epubFilePath: String
    ): Pair<List<String>, String> = withContext(Dispatchers.IO) {
        try {
            debug { "Starting file uploads for item: $itemId" }

            val coverFiles = coverImagePaths.map { JavaFile(it) }
            val epubFile = JavaFile(epubFilePath)

            // Upload all cover images
            val coverUrls = coverFiles.map { coverFile ->
                val coverFileName = "covers/$itemId/${UUID.randomUUID()}.${coverFile.extension}"
                uploadFile(
                    bucket = "items",
                    objectName = coverFileName,
                    file = coverFile,
                    contentType = "image/${coverFile.extension}"
                )
            }
            debug { "Cover uploads successful" }

            // Upload epub
            debug { "Uploading EPUB file..." }
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
        val supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImtrZW9zZ25yYWd6cGdzYmFvY2psIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTcyODc1Mjk5NCwiZXhwIjoyMDQ0MzI4OTk0fQ.8NddKaJ1s4wIMsH0vU70Hi4UmdWVoNSbVJZ-k4P2uWM"
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
                debug { "Upload failed with response: $responseBody" }
                throw IOException("Upload failed: ${response.status}")
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
} 
package com.kafka.kms.service

import com.kafka.base.SecretsProvider
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType
import dev.gitlive.firebase.firestore.FirebaseFirestore
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpHeaders
import io.ktor.http.isSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.io.IOException
import java.util.UUID
import java.io.File as JavaFile

@Inject
class UploadService(
    private val secretsProvider: SecretsProvider,
    private val firestore: FirebaseFirestore,
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
        val item = ItemDetail(
            itemId = itemId,
            title = title,
            mediaType = mediaType,
            creators = creatorsList,
            languages = languagesList,
            description = description,
            coverImage = coverUrl,
            collections = collectionsList,
            subjects = subjectsList,
            translators = translatorsList,
            rating = 0.0,
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
            path = epubUrl,
            url = epubUrl,
            size = JavaFile(epubFilePath).length()
        )

        withContext(Dispatchers.IO) {
            firestore.runTransaction {
                set(firestore.collection("items").document(itemId), item)
                set(firestore.collection("files").document(file.fileId), file)
                null
            }
        }

        itemId
    }

    private suspend fun uploadFiles(
        itemId: String,
        coverFilePath: String,
        epubFilePath: String
    ): Pair<String, String> = withContext(Dispatchers.IO) {
        try {
            val coverFile = JavaFile(coverFilePath)
            val epubFile = JavaFile(epubFilePath)

            val coverFileName = "covers/$itemId/${UUID.randomUUID()}.${coverFile.extension}"
            val epubFileName = "readables/$itemId/${UUID.randomUUID()}.${epubFile.extension}"

            // Upload cover
            val coverUrl = uploadFile(
                bucket = "kafka-books.appspot.com",
                objectName = coverFileName,
                file = coverFile,
                contentType = "image/${coverFile.extension}"
            )

            // Upload epub
            val epubUrl = uploadFile(
                bucket = "kafka-books.appspot.com",
                objectName = epubFileName,
                file = epubFile,
                contentType = "application/epub+zip"
            )

            coverUrl to epubUrl
        } catch (e: Exception) {
            println("Upload error: ${e.message}")
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

        val apiKey = "AIzaSyBYGSIaD-oP39HC8N23kTTxG03KBx2Wiws"
        val encodedObjectName = java.net.URLEncoder.encode(objectName, "UTF-8").replace("+", "%20")
        val uploadUrl = "https://firebasestorage.googleapis.com/v0/b/$bucket/o?name=$encodedObjectName&key=$apiKey"

        val response = httpClient.post(uploadUrl) {
            headers {
                append(HttpHeaders.ContentType, contentType)
                append(HttpHeaders.ContentLength, file.length().toString())
            }
            setBody(file.readBytes())
        }

        if (!response.status.isSuccess()) {
            throw IOException("Upload failed with status: ${response.status}")
        }

        // Return the download URL
        return "https://firebasestorage.googleapis.com/v0/b/$bucket/o/$encodedObjectName?alt=media&key=$apiKey"
    }

    private val JavaFile.extension: String
        get() = name.substringAfterLast('.', "")
} 
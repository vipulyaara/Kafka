package com.kafka.kms.ui.upload

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType

class UploadScreenState {
    var title by mutableStateOf("")
    var description by mutableStateOf("")
    var longDescription by mutableStateOf("")
    var creators by mutableStateOf("")
    var subjects by mutableStateOf("")
    var publishers by mutableStateOf("")
    var languages by mutableStateOf("")
    var collections by mutableStateOf("")
    var sourceUrls by mutableStateOf("")
    var translators by mutableStateOf("")
    var epubFilePath by mutableStateOf("")
    var coverImagePaths by mutableStateOf(listOf<String>())
    var copyrightText by mutableStateOf("")
    var mediaType by mutableStateOf(MediaType.Text)
    var id by mutableStateOf("")
    var hasEditedId by mutableStateOf(false)
    var contentOpfPath by mutableStateOf("")
    var dateFeatured by mutableStateOf("")
    var isEditMode by mutableStateOf(false)
    var isCopyrighted by mutableStateOf<Boolean?>(true)

    fun updateFromContentOpf(metadata: ContentOpfMetadata) {
        println("Collections are ${metadata.collections}")

        title = metadata.title
        creators = metadata.creators.joinToString(", ")
        description = metadata.description
        longDescription = metadata.longDescription.trim()
        subjects = metadata.subjects
            .flatMap { subject -> subject.split("--").map { it.trim() } }
            .distinct()
            .joinToString(", ")
        publishers = metadata.publishers.joinToString(", ")
        languages = metadata.languages.joinToString(", ")
        copyrightText = metadata.rights
        translators = metadata.translators.joinToString(", ")
        sourceUrls = metadata.sources.joinToString(", ")
        collections = metadata.collections.joinToString(", ")
        isCopyrighted = metadata.isCopyrighted
    }

    fun updateFromFetchedItem(item: ItemDetail) {
        title = item.title
        description = item.description.orEmpty()
        creators = item.creators?.joinToString(", ").orEmpty()
        subjects = item.subjects?.joinToString(", ").orEmpty()
        languages = item.languages?.joinToString(", ").orEmpty()
        collections = item.collections?.joinToString(", ").orEmpty()
        mediaType = item.mediaType
        longDescription = item.description.orEmpty()
        publishers = item.publishers.joinToString(", ")
        translators = item.translators?.joinToString(", ").orEmpty()
        coverImagePaths = item.coverImages?.toList() ?: item.coverImage?.let { listOf(it) } ?: emptyList()
        isEditMode = true
        hasEditedId = true
        isCopyrighted = item.copyright
        copyrightText = item.copyrightText.orEmpty()
    }

    fun clearFields() {
        title = ""
        description = ""
        longDescription = ""
        creators = ""
        subjects = ""
        publishers = ""
        languages = ""
        collections = ""
        sourceUrls = ""
        translators = ""
        epubFilePath = ""
        coverImagePaths = listOf()
        copyrightText = ""
        mediaType = MediaType.Text
        id = ""
        hasEditedId = false
        contentOpfPath = ""
        dateFeatured = ""
        isEditMode = false
        isCopyrighted = true
    }

    fun updateIdFromTitleAndCreator() {
        if (!hasEditedId && title.isNotEmpty() && creators.isNotEmpty()) {
            val firstCreator = creators.split(",")[0].trim()
            id = buildString {
                append(
                    firstCreator.lowercase()
                        .replace(Regex("[^a-z0-9]"), "_")
                        .replace(Regex("_+"), "_")
                        .trim('_')
                )
                append("-")
                append(
                    title.lowercase()
                        .replace(Regex("[^a-z0-9]"), "_")
                        .replace(Regex("_+"), "_")
                        .trim('_')
                )
            }
        }
    }
}

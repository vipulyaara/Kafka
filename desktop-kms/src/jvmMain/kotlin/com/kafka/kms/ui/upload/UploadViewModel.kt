package com.kafka.kms.ui.upload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType
import com.kafka.kms.data.remote.SupabaseUploadService
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UploadViewModel(private val uploadService: SupabaseUploadService) : ViewModel() {

    fun fetchItem(itemId: String): Result<Pair<Item, ItemDetail>> {
        var result: Result<Pair<Item, ItemDetail>>? = null

        viewModelScope.launch {
            result = uploadService.fetchItem(itemId)
        }

        return result ?: Result.failure(IllegalStateException("Failed to fetch item"))
    }

    fun uploadBook(
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
        copyright: String,
        isUpdate: Boolean
    ): Result<Unit> {
        var result: Result<Unit>? = null

        viewModelScope.launch {
            result = uploadService.uploadBook(
                itemId = itemId,
                title = title,
                description = description,
                longDescription = longDescription,
                creators = creators,
                subjects = subjects,
                publishers = publishers,
                languages = languages,
                collections = collections,
                translators = translators,
                coverImagePaths = coverImagePaths,
                epubFilePath = epubFilePath,
                mediaType = mediaType,
                copyright = copyright,
                isUpdate = isUpdate
            )
        }

        return result ?: Result.failure(IllegalStateException("Failed to fetch item"))
    }
}
package com.kafka.kms.ui.upload

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType
import com.kafka.kms.data.remote.SupabaseUploadService
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class UploadViewModel(private val uploadService: SupabaseUploadService) : ViewModel() {
    val fetchState = mutableStateOf<FetchState>(FetchState.Initial)
    val uploadState = mutableStateOf<UploadState>(UploadState.Initial)
    val contentOpfState = mutableStateOf<ContentOpfState>(ContentOpfState.Initial)

    fun fetchItem(itemId: String) {
        viewModelScope.launch {
            fetchState.value = FetchState.Loading
            uploadService.fetchItem(itemId)
                .onSuccess { (_, detail) ->
                    fetchState.value = FetchState.Success(detail)
                }
                .onFailure { error ->
                    fetchState.value = FetchState.Error(error.message ?: "Unknown error occurred")
                }
        }
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
    ) {
        viewModelScope.launch {
            uploadState.value = UploadState.Loading
            uploadService.uploadBook(
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
            ).onSuccess {
                uploadState.value = UploadState.Success
            }.onFailure { error ->
                uploadState.value = UploadState.Error(error.message ?: "Unknown error occurred")
            }
        }
    }

    fun parseContentOpf(path: String) {
        viewModelScope.launch {
            contentOpfState.value = ContentOpfState.Loading
            try {
                val metadata = ContentOpfParser.parse(path)
                contentOpfState.value = ContentOpfState.Success(metadata)
            } catch (e: Exception) {
                contentOpfState.value = ContentOpfState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    fun resetUploadState() {
        uploadState.value = UploadState.Initial
    }

    fun resetFetchState() {
        fetchState.value = FetchState.Initial
    }

    fun resetAllStates() {
        fetchState.value = FetchState.Initial
        uploadState.value = UploadState.Initial
        contentOpfState.value = ContentOpfState.Initial
    }
}

sealed interface FetchState {
    data object Initial : FetchState
    data object Loading : FetchState
    data class Success(val data: ItemDetail) : FetchState
    data class Error(val message: String) : FetchState
}

sealed interface UploadState {
    data object Initial : UploadState
    data object Loading : UploadState
    data object Success : UploadState
    data class Error(val message: String) : UploadState
}

sealed interface ContentOpfState {
    data object Initial : ContentOpfState
    data object Loading : ContentOpfState
    data class Success(val metadata: ContentOpfMetadata) : ContentOpfState
    data class Error(val message: String) : ContentOpfState
}

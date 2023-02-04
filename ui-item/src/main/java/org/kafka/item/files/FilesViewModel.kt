package org.kafka.item.files

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.File
import com.sarahang.playback.core.models.Audio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import org.kafka.base.debug
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.domain.observers.ObserveDownloadedItems
import org.kafka.domain.observers.ObserveFiles
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    observeFiles: ObserveFiles,
    savedStateHandle: SavedStateHandle,
    observeDownloadedItems: ObserveDownloadedItems,
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val itemId: String = checkNotNull(savedStateHandle["itemId"])

    val state: StateFlow<FilesViewState> = combine(
        observeFiles.flow,
        observeDownloadedItems.flow,
        loadingState.observable,
        uiMessageManager.message,
    ) { files, downloads, isLoading, message ->
        debug { "OItems $downloads" }
        FilesViewState(
            files = files,
            title = files.firstOrNull()?.itemTitle.orEmpty(),
            downloads = downloads,
            isLoading = isLoading,
            message = message
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = FilesViewState(),
    )

    init {
        observeFiles(ObserveFiles.Param(itemId))
        observeDownloadedItems(Unit)
    }
}

fun File.asAudio() = Audio(
    id = fileId,
    title = title,
    artist = creator,
    album = itemTitle,
    duration = duration,
    playbackUrl = playbackUrl.orEmpty(),
    coverImage = coverImage
)

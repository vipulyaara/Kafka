package org.kafka.item.files

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.File
import com.kafka.data.entities.isAudio
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.models.Audio
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.common.UiMessageManager
import org.kafka.domain.interactors.AddRecentItem
import org.kafka.domain.observers.ObserveDownloadedItems
import org.kafka.domain.observers.ObserveFiles
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    observeFiles: ObserveFiles,
    savedStateHandle: SavedStateHandle,
    observeDownloadedItems: ObserveDownloadedItems,
    private val addRecentItem: AddRecentItem,
    private val playbackConnection: PlaybackConnection,
    private val navigator: Navigator,
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val uiMessageManager = UiMessageManager()
    private val itemId: String = checkNotNull(savedStateHandle["itemId"])
    var selectedExtension by mutableStateOf(null as String?)

    val state: StateFlow<FilesViewState> = combine(
        observeFiles.flow,
        observeDownloadedItems.flow,
        snapshotFlow { selectedExtension },
        loadingState.observable,
        uiMessageManager.message,
    ) { files, downloads, selectedExtension, isLoading, message ->
        FilesViewState(
            title = files.firstOrNull()?.itemTitle.orEmpty(),
            files = files,
            filteredFiles = filteredFiles(files, selectedExtension),
            actionLabels = actionLabels(files),
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

    fun onFileClicked(file: File) {
        viewModelScope.launch {
            addRecentItem(AddRecentItem.Params(file.itemId)).collect()
        }

        if (file.isAudio()) {
            playbackConnection.playAudio(file.asAudio())
        } else {
            navigator.navigate(
                LeafScreen.Reader.buildRoute(file.fileId, navigator.currentRoot.value)
            )
        }
    }

    private fun actionLabels(files: List<File>) = listOf("all") + files.map { it.format }.distinct()

    private fun filteredFiles(files: List<File>, selectedExtension: String?) =
        files.filter { selectedExtension == null || it.format == selectedExtension }
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

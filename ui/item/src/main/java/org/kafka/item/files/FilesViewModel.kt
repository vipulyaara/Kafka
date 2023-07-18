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
import org.kafka.analytics.logger.Analytics
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.ObservableLoadingCounter
import org.kafka.domain.interactors.recent.AddRecentItem
import org.kafka.domain.observers.ObserveFiles
import org.kafka.domain.observers.library.ObserveDownloadedItems
import org.kafka.navigation.Navigator
import org.kafka.navigation.Screen
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

@HiltViewModel
class FilesViewModel @Inject constructor(
    observeFiles: ObserveFiles,
    savedStateHandle: SavedStateHandle,
    observeDownloadedItems: ObserveDownloadedItems,
    private val addRecentItem: AddRecentItem,
    private val playbackConnection: PlaybackConnection,
    private val downloader: Downloader,
    private val navigator: Navigator,
    private val analytics: Analytics
) : ViewModel() {
    private val loadingState = ObservableLoadingCounter()
    private val itemId: String = checkNotNull(savedStateHandle["itemId"])
    var selectedExtension by mutableStateOf(null as String?)

    val state: StateFlow<FilesViewState> = combine(
        observeFiles.flow,
        observeDownloadedItems.flow,
        snapshotFlow { selectedExtension },
        loadingState.observable
    ) { files, downloads, selectedExtension, isLoading ->
        FilesViewState(
            title = files.firstOrNull()?.itemTitle.orEmpty(),
            files = files,
            filteredFiles = filteredFiles(files, selectedExtension),
            actionLabels = actionLabels(files),
            downloads = downloads,
            isLoading = isLoading
        )
    }.stateInDefault(scope = viewModelScope, initialValue = FilesViewState())

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
                Screen.Reader.createRoute(navigator.currentRoot.value, file.fileId)
            )
        }
    }

    fun onDownloadClicked(file: File) {
        analytics.log { downloadFile(file.fileId, file.itemId) }
        viewModelScope.launch { downloader.enqueueFile(file.fileId) }
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

package com.kafka.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.item.ItemWithDownload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.debug
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.UiMessageManager
import org.kafka.common.snackbar.UiMessage
import org.kafka.domain.observers.ObserveReadableRecentItem
import org.kafka.domain.observers.library.ObserveDownloadItem
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val observeDownloadItem: ObserveDownloadItem,
    private val observeRecentItem: ObserveReadableRecentItem,
    private val downloader: Downloader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    private var fileId = savedStateHandle.getStateFlow("fileId", "")

    val readerState = combine(
        observeDownloadItem.flow,
        observeRecentItem.flow,
        uiMessageManager.message,
    ) { download, readable, message ->
        debug { "Readable is $readable" }
        ReaderViewState(
            recentItem = readable,
            download = download,
            message = message
        )
    }.stateInDefault(
        scope = viewModelScope,
        initialValue = ReaderViewState(),
    )

    init {
        viewModelScope.launch(Dispatchers.IO) {
            downloader.enqueueFile(fileId.value)
        }

        viewModelScope.launch {
            observeDownloadItem(fileId.value)
        }

        viewModelScope.launch {
            observeRecentItem(fileId.value)
        }
    }
}

data class ReaderViewState(
    val recentItem: RecentItem.Readable? = null,
    val download: ItemWithDownload? = null,
    val message: UiMessage? = null,
)

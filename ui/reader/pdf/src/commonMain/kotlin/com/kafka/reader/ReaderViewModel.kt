package com.kafka.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.debug
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.UiMessageManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.entities.RecentTextItem
import com.kafka.data.feature.item.ItemWithDownload
import com.kafka.domain.observers.ObserveRecentTextItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.downloader.interactors.ObserveDownloadByFileId
import javax.inject.Inject

class ReaderViewModel @Inject constructor(
    private val observeDownloadByFileId: ObserveDownloadByFileId,
    private val observeRecentItem: ObserveRecentTextItem,
    private val downloader: Downloader,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    private var fileId = savedStateHandle.getStateFlow("fileId", "")

    val readerState = combine(
        observeDownloadByFileId.flow,
        observeRecentItem.flow,
        uiMessageManager.message,
    ) { download, readable, message ->
        debug { "Readable is $readable" }
        ReaderViewState(recentItem = readable, download = download, message = message)
    }.stateInDefault(scope = viewModelScope, initialValue = ReaderViewState())

    init {
        viewModelScope.launch(Dispatchers.IO) {
            downloader.enqueueFile(fileId.value)
        }

        viewModelScope.launch {
            observeDownloadByFileId(fileId.value)
        }

        viewModelScope.launch {
            observeRecentItem(fileId.value)
        }
    }
}

data class ReaderViewState(
    val recentItem: RecentTextItem? = null,
    val download: ItemWithDownload? = null,
    val message: UiMessage? = null,
)

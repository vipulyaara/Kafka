package com.kafka.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.entities.RecentTextItem
import com.kafka.data.feature.item.ItemWithDownload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.UiMessage
import org.kafka.common.UiMessageManager
import org.kafka.domain.observers.ObserveDownloadItem
import org.kafka.domain.observers.ObserveTextFile
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    private val observeDownloadItem: ObserveDownloadItem,
    private val observeTextFile: ObserveTextFile,
    private val downloader: Downloader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    private var fileId = savedStateHandle.getStateFlow("fileId", "")

    val readerState = combine(
        observeDownloadItem.flow,
        observeTextFile.flow,
        uiMessageManager.message,
    ) { download, textFile, message ->
        ReaderViewState(
            recentTextItem = textFile,
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
            observeTextFile(fileId.value)
        }
    }
}

data class ReaderViewState(
    val recentTextItem: RecentTextItem? = null,
    val download: ItemWithDownload? = null,
    val message: UiMessage? = null,
)

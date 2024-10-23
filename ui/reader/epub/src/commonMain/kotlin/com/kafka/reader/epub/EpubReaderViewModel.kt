package com.kafka.reader.epub

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.domain.interactors.UpdateCurrentPage
import com.kafka.domain.observers.ObserveRecentTextItem
import com.kafka.reader.epub.domain.ParseEbook
import com.kafka.reader.epub.models.EpubBook
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.downloader.interactors.ObserveDownloadByFileId
import javax.inject.Inject

class EpubReaderViewModel @Inject constructor(
    private val observeDownloadByFileId: ObserveDownloadByFileId,
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val observeRecentItem: ObserveRecentTextItem,
    private val updateCurrentPage: UpdateCurrentPage,
    private val snackbarManager: SnackbarManager,
    private val parseEbook: ParseEbook,
    private val downloader: Downloader,
    dispatchers: CoroutineDispatchers,
) : ViewModel() {
    private val fileId = savedStateHandle.get<String>("fileId")!!
    private var ebook by mutableStateOf<EpubBook?>(null)

    val state = combine(
        parseEbook.inProgress,
        snapshotFlow { ebook }
    ) { loading, ebook ->
        EpubState(loading = loading, epubBook = ebook)
    }.stateInDefault(viewModelScope, EpubState())

    val lazyListState = LazyListState()

    init {
        observeRecentItem(fileId)
        observeDownloadByFileId(fileId)

        viewModelScope.launch(dispatchers.io) {
            downloader.enqueueFile(fileId)
        }

        viewModelScope.launch {
            combine(observeRecentItem.flow, observeDownloadByFileId.flow) { item, download ->
                if (item != null && download?.downloadInfo?.status == DownloadStatus.COMPLETED) {
                    loadEbook(item.localUri)
                }
            }
        }
    }

    private fun onPageChanged(fileId: String, page: Int) {
        viewModelScope.launch {
            updateCurrentPage(UpdateCurrentPage.Params(fileId, page))
        }
    }

    private fun loadEbook(uri: String) {
        viewModelScope.launch {
            val result = parseEbook(uri)
            result.onSuccess { ebook = it }
            result.onFailure { snackbarManager.addMessage(UiMessage.Error(it)) }
        }
    }
}

data class EpubState(
    val loading: Boolean = false,
    val epubBook: EpubBook? = null
)

internal fun chunkText(text: String): List<String> {
    return text.splitToSequence("\n\n")
        .filter { it.isNotBlank() }
        .toList()
}

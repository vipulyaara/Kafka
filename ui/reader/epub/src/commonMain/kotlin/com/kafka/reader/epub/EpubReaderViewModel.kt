package com.kafka.reader.epub

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.entities.Download
import com.kafka.domain.interactors.UpdateCurrentPage
import com.kafka.downloader.core.DownloadItem
import com.kafka.downloader.core.ObserveDownload
import com.kafka.reader.epub.domain.ParseEbook
import com.kafka.reader.epub.models.EpubBook
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import javax.inject.Inject

class EpubReaderViewModel @Inject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val updateCurrentPage: UpdateCurrentPage,
    private val observeDownload: ObserveDownload,
    private val snackbarManager: SnackbarManager,
    private val downloadItem: DownloadItem,
    private val parseEbook: ParseEbook,
) : ViewModel() {
    private val fileId = savedStateHandle.get<String>("fileId")!!
    private var ebook by mutableStateOf<EpubBook?>(null)

    val state = combine(
        combine(
            parseEbook.inProgress,
            downloadItem.inProgress
        ) { loadings -> loadings.any { it } },
        snapshotFlow { ebook }
    ) { loading, ebook ->
        EpubState(loading = loading, epubBook = ebook)
    }.stateInDefault(viewModelScope, EpubState())

    val lazyListState = LazyListState()

    init {
        observeDownload(fileId)

        viewModelScope.launch {
            downloadItem(fileId)
        }

        viewModelScope.launch {
            observeDownload.flow.collectLatest { download ->
                if (download != null && download.status == Download.Status.Completed) {
                    loadEbook(download.filePath)
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

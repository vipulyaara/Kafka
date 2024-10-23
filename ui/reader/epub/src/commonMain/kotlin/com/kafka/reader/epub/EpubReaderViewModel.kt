package com.kafka.reader.epub

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.debug
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.entities.Download
import com.kafka.domain.interactors.UpdateCurrentPage
import com.kafka.downloader.core.KtorDownloader
import com.kafka.downloader.core.ObserveDownload
import com.kafka.reader.epub.domain.ParseEbook
import com.kafka.reader.epub.models.EpubBook
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import java.io.File
import javax.inject.Inject

class EpubReaderViewModel @Inject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val updateCurrentPage: UpdateCurrentPage,
    private val observeDownload: ObserveDownload,
    private val snackbarManager: SnackbarManager,
    private val downloader: KtorDownloader,
    private val parseEbook: ParseEbook,
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
        observeDownload(fileId)

        viewModelScope.launch {
            downloader.download(fileId)
        }

        viewModelScope.launch {
            observeDownload.flow.collectLatest {
                debug { "Download is $it" }
                if (it != null && it.status == Download.Status.Completed) {
                    val file = File(it.filePath)
                    if (file.exists()) {
                        debug { "existing" }
                    } else {
                        debug { "Not existing" }
                    }
                    loadEbook(it.filePath)
                }
            }
        }

//        observeRecentItem(fileId)
//        observeDownloadByFileId(fileId)

//        viewModelScope.launch(dispatchers.io) {
//            downloader.enqueueFile(fileId)
//        }

//        viewModelScope.launch {
//            combine(observeRecentItem.flow, observeDownloadByFileId.flow) { item, download ->
//                if (item != null && download?.downloadInfo?.status == DownloadStatus.COMPLETED) {
//                    loadEbook(item.localUri)
//                }
//            }
//        }
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

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
import com.kafka.domain.interactors.UpdateCurrentPage
import com.kafka.domain.observers.ObserveRecentTextItem
import com.kafka.reader.epub.domain.ParseEbook
import com.kafka.reader.epub.models.EpubBook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

class EpubReaderViewModel @Inject constructor(
    private val parseEbook: ParseEbook,
    private val snackbarManager: SnackbarManager,
    private val observeRecentItem: ObserveRecentTextItem,
    private val updateCurrentPage: UpdateCurrentPage,
    private val downloader: Downloader,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var ebook by mutableStateOf<EpubBook?>(null)

    val state = combine(
        observeRecentItem.flow.filterNotNull(),
        parseEbook.inProgress,
        snapshotFlow { ebook }
    ) { recentItem, loading, ebook ->
        EpubState(
            currentPage = recentItem.currentPage,
            localUrl = recentItem.localUri, loading = loading,
            epubBook = ebook
        )
    }.stateInDefault(viewModelScope, EpubState())

    val lazyListState = LazyListState()

    fun load(fileId: String) {
        viewModelScope.launch {
            observeRecentItem(fileId)
        }

        viewModelScope.launch(Dispatchers.IO) {
            debug { "EpubReaderViewModel enqueue" }
            downloader.enqueueFile(fileId)
        }
    }

    private fun onPageChanged(fileId: String, page: Int) {
        viewModelScope.launch {
            updateCurrentPage(UpdateCurrentPage.Params(fileId, page)).collect()
        }
    }

    fun loadEbook(uri: String) {
        viewModelScope.launch {
            val result = parseEbook(uri)
            result.onSuccess { ebook = it }
            result.onFailure { snackbarManager.addMessage(UiMessage.Error(it)) }
        }
    }
}

data class EpubState(
    val currentPage: Int = 0,
    val localUrl: String? = null,
    val loading: Boolean = false,
    val epubBook: EpubBook? = null
)

internal fun chunkText(text: String): List<String> {
    return text.splitToSequence("\n\n")
        .filter { it.isNotBlank() }
        .toList()
}

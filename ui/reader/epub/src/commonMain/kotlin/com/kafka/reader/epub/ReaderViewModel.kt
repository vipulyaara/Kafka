package com.kafka.reader.epub

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.platform.ShareUtils
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.entities.Download
import com.kafka.domain.interactors.GetLastPageOffset
import com.kafka.domain.interactors.GetLastSeenPage
import com.kafka.domain.interactors.UpdateCurrentPage
import com.kafka.domain.interactors.UpdateCurrentPageOffset
import com.kafka.domain.interactors.library.AddHighlight
import com.kafka.domain.interactors.reader.ParseEbook
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.domain.observers.library.ObserveHighlights
import com.kafka.downloader.core.DownloadItem
import com.kafka.downloader.core.ObserveDownload
import com.kafka.navigation.Navigator
import com.kafka.navigation.deeplink.DeepLinks
import com.kafka.navigation.graph.Screen
import com.kafka.reader.epub.domain.ObserveReaderSettings
import com.kafka.reader.epub.domain.UpdateReaderSettings
import com.kafka.reader.epub.settings.ReaderSettings
import kafka.reader.core.models.EpubBook
import kafka.reader.core.models.TextHighlight
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class ReaderViewModel(
    observeItemDetail: ObserveItemDetail,
    @Assisted private val savedStateHandle: SavedStateHandle,
    observeReaderSettings: ObserveReaderSettings,
    private val updateReaderSettings: UpdateReaderSettings,
    private val updateCurrentPage: UpdateCurrentPage,
    private val updateCurrentPageOffset: UpdateCurrentPageOffset,
    private val observeDownload: ObserveDownload,
    private val observeHighlights: ObserveHighlights,
    private val getLastSeenPage: GetLastSeenPage,
    private val getLastPageOffset: GetLastPageOffset,
    private val addHighlight: AddHighlight,
    private val snackbarManager: SnackbarManager,
    private val downloadItem: DownloadItem,
    private val parseEbook: ParseEbook,
    private val analytics: Analytics,
    private val shareUtils: ShareUtils,
    private val navigator: Navigator
) : ViewModel() {
    private val itemId = savedStateHandle.get<String>("itemId")!!
    private val fileId = savedStateHandle.get<String>("fileId")!!
    private var ebook by mutableStateOf<EpubBook?>(null)

    // TODO - Check if we need this state
    private val lazyListState = LazyListState()

    val state = com.kafka.base.combine(
        combine(
            parseEbook.inProgress,
            downloadItem.inProgress
        ) { loadings -> loadings.any { it } },
        snapshotFlow { ebook },
        observeItemDetail.flow,
        observeDownload.flow,
        observeHighlights.flow,
        observeReaderSettings.flow
    ) { loading, ebook, itemDetail, download, highlights, settings ->
        ReaderState(
            itemId = itemId,
            loading = loading,
            epubBook = ebook,
            language = itemDetail?.language,
            download = download,
            highlights = highlights,
            settings = settings
        )
    }.stateInDefault(viewModelScope, ReaderState())

    init {
        observeDownload(fileId)
        observeHighlights(ObserveHighlights.Params(itemId))
        observeItemDetail(ObserveItemDetail.Param(itemId))
        observeReaderSettings(ObserveReaderSettings.Params(itemId))

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

    fun onPageChanged(page: Int) {
        onPageChanged(fileId, page)
    }

    private fun onPageChanged(fileId: String, page: Int) {
        viewModelScope.launch {
            updateCurrentPage(UpdateCurrentPage.Params(fileId, page))
        }
    }

    fun onPageScrolled(offset: Int) {
        viewModelScope.launch {
            onPageOffsetChanged(fileId, offset)
        }
    }

    private fun onPageOffsetChanged(fileId: String, offset: Int) {
        viewModelScope.launch {
            updateCurrentPageOffset(UpdateCurrentPageOffset.Params(fileId, offset))
        }
    }

    fun navigate(url: String) {
        val chapterIds = state.value.epubBook?.chapters?.map { it.chapterId }.orEmpty()
        val chapterId = chapterIds.find {
            it == url || it == url.substringAfter("#")
        }

        if (chapterId != null) {
            viewModelScope.launch {
                lazyListState.scrollToItem(chapterIds.indexOf(chapterId))
            }
        } else {
            navigator.navigate(Screen.Web(url))
        }
    }

    private fun loadEbook(path: String) {
        viewModelScope.launch {
            val result = parseEbook(ParseEbook.Params(filePath = path))

            result.onSuccess {
                val lastSeenPage = getLastSeenPage(GetLastSeenPage.Params(fileId)).getOrNull() ?: 0
                val lastPageOffset =
                    getLastPageOffset(GetLastPageOffset.Params(fileId)).getOrNull() ?: 0
                ebook = it.copy(lastSeenPage = lastSeenPage, lastPageOffset = lastPageOffset)
            }
            result.onException { snackbarManager.addMessage(UiMessage.Error(it)) }
        }
    }

    fun shareItemText(context: Any?) {
        analytics.log { this.shareItem(itemId, "item_detail") }
        val itemTitle = state.value.epubBook?.title

        val link = DeepLinks.find(Screen.ItemDetail(itemId))
        val text = "\nCheck out $itemTitle on Kafka\n\n$link\n"

        shareUtils.shareText(text = text, context = context)
    }

    fun addHighlight(highlight: TextHighlight) {
        snackbarManager.addMessage("Highlight added ${highlight.text}")
        viewModelScope.launch {
            addHighlight(AddHighlight.Params(highlight))
        }
    }

    fun updateSettings(newSettings: ReaderSettings) {
        viewModelScope.launch {
            updateReaderSettings(newSettings)
        }
    }
}

data class ReaderState(
    val itemId: String = "",
    val language: String? = null,
    val loading: Boolean = false,
    val epubBook: EpubBook? = null,
    val download: Download? = null,
    val highlights: List<TextHighlight> = emptyList(),
    val settings: ReaderSettings = ReaderSettings.default(ReaderSettings.DEFAULT_LANGUAGE)
) {
    val progress: String?
        get() = download?.progress?.takeIf { it in 1..99 }?.let { "$it%" }
}

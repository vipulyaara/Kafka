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
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.platform.ShareUtils
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.entities.Download
import com.kafka.data.entities.ItemDetail
import com.kafka.domain.interactors.UpdateCurrentPage
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.downloader.core.DownloadItem
import com.kafka.downloader.core.ObserveDownload
import com.kafka.navigation.Navigator
import com.kafka.navigation.deeplink.DeepLinks
import com.kafka.navigation.graph.Screen
import com.kafka.reader.epub.domain.ParseEbook
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.ReaderSettingsRepository
import kafka.reader.core.models.EpubBook
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class EpubReaderViewModel(
    observeItemDetail: ObserveItemDetail,
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val settingsRepository: ReaderSettingsRepository,
    private val updateCurrentPage: UpdateCurrentPage,
    private val observeDownload: ObserveDownload,
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

    val readerSettings = settingsRepository.getSettings()
        .stateInDefault(scope = viewModelScope, initialValue = ReaderSettings())

    val showTocSheet = mutableStateOf(false)
    val lazyListState = LazyListState()

    val state = combine(
        combine(
            parseEbook.inProgress,
            downloadItem.inProgress
        ) { loadings -> loadings.any { it } },
        snapshotFlow { ebook },
        observeItemDetail.flow,
        observeDownload.flow
    ) { loading, ebook, itemDetail, download ->
        val progress = download?.progress?.takeIf { it in 1..99 }?.let { "$it%" }

        EpubState(
            loading = loading,
            epubBook = ebook,
            itemDetail = itemDetail,
            url = download?.url,
            progress = progress
        )
    }.stateInDefault(viewModelScope, EpubState())

    init {
        observeDownload(fileId)
        observeItemDetail(ObserveItemDetail.Param(itemId))

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

    fun goBack() {
        navigator.goBack()
    }

    private fun onPageChanged(fileId: String, page: Int) {
        viewModelScope.launch {
            updateCurrentPage(UpdateCurrentPage.Params(fileId, page))
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

    private fun loadEbook(uri: String) {
        viewModelScope.launch {
            val result = parseEbook(uri)
            result.onSuccess { ebook = it }
            result.onFailure { snackbarManager.addMessage(UiMessage.Error(it)) }
        }
    }

    fun shareItemText(context: Any?) {
        analytics.log { this.shareItem(itemId, "item_detail") }
        val itemTitle = state.value.itemDetail!!.title

        val link = DeepLinks.find(Screen.ItemDetail(itemId))
        val text = "\nCheck out $itemTitle on Kafka\n\n$link\n"

        shareUtils.shareText(text = text, context = context)
    }

    fun updateSettings(newSettings: ReaderSettings) {
        viewModelScope.launch {
            settingsRepository.updateSettings(newSettings)
        }
    }
}

data class EpubState(
    val loading: Boolean = false,
    val epubBook: EpubBook? = null,
    val itemDetail: ItemDetail? = null,
    val progress: String? = null,
    val url: String? = null
)

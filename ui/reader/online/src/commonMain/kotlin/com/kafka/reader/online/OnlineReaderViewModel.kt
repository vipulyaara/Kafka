package com.kafka.reader.online

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.analytics.providers.Analytics
import com.kafka.base.debug
import com.kafka.base.domain.onException
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.platform.ShareUtils
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.data.feature.item.ItemWithDownload
import com.kafka.domain.interactors.GetReaderState
import com.kafka.domain.interactors.ReaderState
import com.kafka.domain.interactors.UpdateCurrentPage
import com.kafka.domain.interactors.getCurrentPageFromReaderUrl
import com.kafka.domain.observers.ObserveItemDetail
import com.kafka.domain.observers.ShouldAutoDownload
import com.kafka.navigation.Navigator
import com.kafka.navigation.deeplink.DeepLinks
import com.kafka.navigation.graph.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.downloader.interactors.ObserveDownloadByFileId
import javax.inject.Inject

class OnlineReaderViewModel @Inject constructor(
    shouldAutoDownload: ShouldAutoDownload,
    observeDownloadByFileId: ObserveDownloadByFileId,
    observeItemDetail: ObserveItemDetail,
    private val getReaderState: GetReaderState,
    private val updateCurrentPage: UpdateCurrentPage,
    private val downloader: Downloader,
    private val navigator: Navigator,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    private val shareUtils: ShareUtils,
    @Assisted savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val itemId: String = savedStateHandle["itemId"]!!
    private val fileId: String = savedStateHandle["fileId"]!!

    val readerState = MutableStateFlow<ReaderState?>(null)
    var showDownloadComplete = mutableStateOf(false)

    val state: StateFlow<OnlineReaderState> = combine(
        readerState,
        observeItemDetail.flow,
        observeDownloadByFileId.flow,
        shouldAutoDownload.flow
    ) { readerState, itemDetail, download, autoDownload ->
        OnlineReaderState(
            readerState = readerState,
            download = download,
            autoDownload = autoDownload,
            showDownloadIcon = !(itemDetail?.isAccessRestricted ?: true)
        )
    }.stateInDefault(viewModelScope, OnlineReaderState())

    init {
        observeItemDetail(ObserveItemDetail.Param(itemId))
        observeDownloadByFileId(fileId)
        shouldAutoDownload(ShouldAutoDownload.Param(itemId, fileId))

        viewModelScope.launch {
            readerState.value = getReaderState.invoke(GetReaderState.Params(itemId, fileId)).also {
                it.onException { snackbarManager.addMessage(UiMessage("Error loading reader")) }
            }.getOrNull()
        }
    }

    fun updateCurrentPage(url: String) {
        viewModelScope.launch {
            if (readerState.value != null) {
                val currentPage = url.getCurrentPageFromReaderUrl()
                val fileId = readerState.value!!.fileId
                updateCurrentPage.invoke(UpdateCurrentPage.Params(fileId, currentPage))
            }
        }
    }

    fun downloadItem(fileId: String) {
        debug { "Downloading item $fileId" }
        analytics.log { this.downloadFile(fileId = fileId, itemId = itemId, source = "reader") }
        viewModelScope.launch(Dispatchers.IO) {
            if (readerState.value != null) {
                downloader.enqueueFile(fileId)
            }
        }
    }

    fun logOpenOfflineReader() {
        analytics.log { readItem(itemId = itemId, type = "offline") }
    }

    fun shareItem(context: Any?) {
        analytics.log { this.shareItem(itemId = itemId, source = "reader") }
        val itemTitle = readerState.value?.itemTitle

        val link = DeepLinks.find(Screen.ItemDetail(itemId))
        val text = "\nCheck out $itemTitle on Kafka\n\n$link\n"

        shareUtils.shareText(text, context)
    }

    fun goBack() {
        navigator.goBack()
    }
}

data class OnlineReaderState(
    val readerState: ReaderState? = null,
    val download: ItemWithDownload? = null,
    val autoDownload: Boolean = false,
    val showDownloadIcon: Boolean = false,
)

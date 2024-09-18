package com.kafka.reader.online

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.kafka.data.feature.item.ItemWithDownload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.kafka.analytics.logger.Analytics
import org.kafka.base.debug
import org.kafka.base.domain.onException
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.asUiMessage
import org.kafka.common.shareText
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.domain.interactors.GetReaderState
import org.kafka.domain.interactors.ReaderState
import org.kafka.domain.interactors.UpdateCurrentPage
import org.kafka.domain.interactors.getCurrentPageFromReaderUrl
import org.kafka.domain.observers.ShouldAutoDownload
import org.kafka.domain.observers.library.ObserveDownloadByFileId
import org.kafka.navigation.Navigator
import org.kafka.navigation.deeplink.DeepLinksNavigation
import org.kafka.navigation.deeplink.Navigation
import org.kafka.navigation.graph.Screen
import org.kafka.reader.R
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

@HiltViewModel
class OnlineReaderViewModel @Inject constructor(
    shouldAutoDownload: ShouldAutoDownload,
    observeDownloadByFileId: ObserveDownloadByFileId,
    private val getReaderState: GetReaderState,
    private val updateCurrentPage: UpdateCurrentPage,
    private val downloader: Downloader,
    private val navigator: Navigator,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val route = savedStateHandle.toRoute<Screen.OnlineReader>()
    private val itemId: String = route.itemId
    private val fileId: String = route.fileId

    val readerState = MutableStateFlow<ReaderState?>(null)
    var showDownloadComplete = mutableStateOf(false)

    val state: StateFlow<OnlineReaderState> = combine(
        readerState,
        observeDownloadByFileId.flow,
        shouldAutoDownload.flow
    ) { readerState, download, autoDownload ->
        OnlineReaderState(
            readerState = readerState,
            download = download,
            autoDownload = autoDownload
        )
    }.stateInDefault(viewModelScope, OnlineReaderState())

    init {
        observeDownloadByFileId(fileId)
        shouldAutoDownload(ShouldAutoDownload.Param(itemId))

        viewModelScope.launch {
            readerState.value = getReaderState.invoke(itemId).also {
                it.onException { snackbarManager.addMessage("Error loading reader".asUiMessage()) }
            }.getOrNull()
        }
    }

    fun updateCurrentPage(url: String) {
        viewModelScope.launch {
            if (readerState.value != null) {
                val currentPage = url.getCurrentPageFromReaderUrl()
                val fileId = readerState.value!!.fileId
                updateCurrentPage.invoke(UpdateCurrentPage.Params(fileId, currentPage)).collect()
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

    fun shareItem(context: Context) {
        analytics.log { this.shareItem(itemId = itemId, source = "reader") }
        val itemTitle = readerState.value?.itemTitle

        val link = DeepLinksNavigation.findUri(Navigation.ItemDetail(itemId)).toString()
        val text = context.getString(R.string.check_out_on_kafka, itemTitle, link).trimIndent()

        context.shareText(text)
    }

    fun goBack() {
        navigator.goBack()
    }
}

data class OnlineReaderState(
    val readerState: ReaderState? = null,
    val download: ItemWithDownload? = null,
    val autoDownload: Boolean = false,
)

@file:Suppress("DEPRECATION")

package com.kafka.reader.online

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewState
import com.kafka.data.feature.item.ItemWithDownload
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kafka.analytics.logger.Analytics
import org.kafka.base.domain.onException
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.asUiMessage
import org.kafka.common.shareText
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.toast
import org.kafka.domain.interactors.GetReaderState
import org.kafka.domain.interactors.ReaderState
import org.kafka.domain.interactors.UpdateCurrentPage
import org.kafka.domain.interactors.getCurrentPageFromReaderUrl
import org.kafka.domain.observers.library.ObserveDownloadByItemId
import org.kafka.navigation.Navigator
import org.kafka.navigation.deeplink.DeepLinksNavigation
import org.kafka.navigation.deeplink.Navigation
import org.kafka.reader.R
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

@HiltViewModel
class OnlineReaderViewModel @Inject constructor(
    observeDownloadByItemId: ObserveDownloadByItemId,
    private val getReaderState: GetReaderState,
    private val updateCurrentPage: UpdateCurrentPage,
    private val downloader: Downloader,
    private val navigator: Navigator,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val itemId: String = savedStateHandle["itemId"] ?: error("Url not provided")
    private val readerState = MutableStateFlow<ReaderState?>(null)
    private val urlState = MutableStateFlow("")

    val webViewState = readerState.map { state ->
        state?.url?.let { WebViewState(WebContent.Url(url = it)) }
    }.stateInDefault(viewModelScope, null)

    val state: StateFlow<OnlineReaderState> = combine(
        urlState,
        readerState,
        observeDownloadByItemId.flow
    ) { url, readerState, download ->
        OnlineReaderState(
            url = url,
            readerState = readerState,
            download = download
        )
    }.stateInDefault(viewModelScope, OnlineReaderState())

    init {
        observeDownloadByItemId(ObserveDownloadByItemId.Params(itemId))

        viewModelScope.launch {
            readerState.value = getReaderState.invoke(itemId).also {
                it.onException { snackbarManager.addMessage("Error loading reader".asUiMessage()) }
            }.getOrNull()
        }

        viewModelScope.launch {
            urlState.collectLatest { url ->
                updateCurrentPage(url)
            }
        }
    }

    fun updateUrl(url: String) {
        urlState.value = url
    }

    private fun updateCurrentPage(url: String) {
        viewModelScope.launch {
            if (readerState.value != null) {
                val currentPage = url.getCurrentPageFromReaderUrl()
                val fileId = readerState.value!!.fileId
                updateCurrentPage.invoke(UpdateCurrentPage.Params(fileId, currentPage)).collect()
            }
        }
    }

    fun downloadItem(fileId:String, context: Context) {
        context.toast("Downloading in background...")
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
    val url: String = "",
    val readerState: ReaderState? = null,
    val download: ItemWithDownload? = null
)

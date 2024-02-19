package com.kafka.reader.online

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.kafka.analytics.logger.Analytics
import org.kafka.base.domain.onException
import org.kafka.base.extensions.stateInDefault
import org.kafka.common.asUiMessage
import org.kafka.common.shareText
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.domain.interactors.GetReaderState
import org.kafka.domain.interactors.ReaderState
import org.kafka.domain.interactors.UpdateCurrentPage
import org.kafka.domain.interactors.getCurrentPageFromReaderUrl
import org.kafka.navigation.Navigator
import org.kafka.navigation.deeplink.DeepLinksNavigation
import org.kafka.navigation.deeplink.Navigation
import org.kafka.reader.R
import javax.inject.Inject

@HiltViewModel
class OnlineReaderViewModel @Inject constructor(
    private val getReaderState: GetReaderState,
    private val updateCurrentPage: UpdateCurrentPage,
    private val navigator: Navigator,
    private val snackbarManager: SnackbarManager,
    private val analytics: Analytics,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val itemId: String = savedStateHandle["itemId"] ?: error("Url not provided")
    private val readerState = MutableStateFlow<ReaderState?>(null)

    val urlState = MutableStateFlow("")

    val webViewState = readerState.map { state ->
        state?.let { WebViewState(WebContent.Url(url = it.url)) }
    }.distinctUntilChanged().stateInDefault(viewModelScope, null)

    init {
        viewModelScope.launch {
            val result = getReaderState.invoke(itemId)
            readerState.value = result.getOrNull()

            result.onException {
                snackbarManager.addMessage("Error loading reader".asUiMessage())
            }
        }

        viewModelScope.launch {
            urlState.collectLatest { url ->
                updateCurrentPage(url)
            }
        }
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

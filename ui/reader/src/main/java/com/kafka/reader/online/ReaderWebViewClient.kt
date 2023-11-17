package com.kafka.reader.online

import android.webkit.WebView
import com.google.accompanist.web.AccompanistWebViewClient
import kotlinx.coroutines.flow.MutableStateFlow

class ReaderWebViewClient(
    private val urlState: MutableStateFlow<String>
) : AccompanistWebViewClient() {

    override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
        urlState.value = url.orEmpty()
        super.doUpdateVisitedHistory(view, url, isReload)
    }
}

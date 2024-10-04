package com.kafka.reader.online

import android.webkit.WebView
import com.google.accompanist.web.AccompanistWebViewClient
import com.kafka.base.debug

class ReaderWebViewClient(
    private val updateUrl: (String) -> Unit
) : AccompanistWebViewClient() {

    override fun doUpdateVisitedHistory(view: WebView, url: String?, isReload: Boolean) {
        updateUrl(url.orEmpty())
        debug { "Reader url changed: $url" }
        super.doUpdateVisitedHistory(view, url, isReload)
    }
}

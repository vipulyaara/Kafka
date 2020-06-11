package com.kafka.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import com.data.base.extensions.debug
import com.kafka.ui_common.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_webview.*

@AndroidEntryPoint
class ReaderFragment : BaseFragment() {
    private val viewModel: ReaderViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureWebView()

        viewModel.preparePdfUrl(Reader.url ?: "").let {
            webView.loadUrl(viewModel.preparePdfUrl(Reader.url ?: ""))
            debug { it }
        }
    }

    private fun configureWebView() {
        webView.apply {
            isVerticalScrollBarEnabled = true
            settings.javaScriptEnabled = true
            settings.setSupportZoom(true)
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    debug { progress.toString() }
                    progressBar?.progress = progress
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_webview, container, false)
    }
}

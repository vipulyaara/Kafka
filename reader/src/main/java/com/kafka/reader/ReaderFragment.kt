package com.kafka.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.data.base.extensions.debug
import com.kafka.ui_common.BaseFragment
import com.kafka.ui_common.showSnackbar
import com.thefinestartist.finestwebview.FinestWebView
import kotlinx.android.synthetic.main.fragment_webview.*
import javax.inject.Inject

class ReaderFragment : BaseFragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ReaderViewModel by viewModels(factoryProducer = { viewModelFactory })

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

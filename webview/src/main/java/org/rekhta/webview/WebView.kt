package org.rekhta.webview

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import org.kafka.common.widgets.DefaultScaffold

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(url: String, title: String = "") {
    val webViewState = rememberWebViewState(url)

    DefaultScaffold(topBar = {
        Column {
            AnimatedVisibility(webViewState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }) {
        WebView(
            state = webViewState,
            onCreated = {
                it.settings.javaScriptEnabled = true
            }
        )
    }
}

//@Composable
//private fun AndøroidWebView(context: Context) {
//    AndroidView(factory = {
//        WebView(context).apply {
//            applySettings()
//            webViewClient = object : WebViewClient() {}
//            webChromeClient = object : WebChromeClient() {
//                override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                    super.onProgressChanged(view, newProgress)
//                    progress = newProgress / 100f
//                }
//            }
//
//            loadUrl(url)
//        }
//    })
//}
//
//private fun WebView.applySettings() = settings.apply {
//    javaScriptCanOpenWindowsAutomatically = true
//    domStorageEnabled = true
//    builtInZoomControls = true
//    displayZoomControls = false
//    allowFileAccessFromFileURLs = true
//    allowUniversalAccessFromFileURLs = true
//    allowFileAccess = true
//    allowContentAccess = true
//}

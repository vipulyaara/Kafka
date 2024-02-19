package org.kafka.webview

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import org.kafka.base.debug
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.material.BackButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.scaffoldPadding

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebView(url: String, goBack: () -> Unit) {
    val webViewState = rememberWebViewState(url)
    val clipboardManager = LocalClipboardManager.current

    debug { "Opening web page with url $url" }

    Scaffold(topBar = {
        Column {
            TopBar(
                title = webViewState.pageTitle.orEmpty(),
                navigationIcon = { BackButton { goBack() } },
                actions = {
                    if (webViewState.lastLoadedUrl != null) {
                        val lastLoadedUrl = webViewState.lastLoadedUrl.orEmpty()
                        CopyUrlIcon { clipboardManager.setText(AnnotatedString(lastLoadedUrl)) }
                    }
                }
            )
            AnimatedVisibility(webViewState.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }) { paddingValues ->
        ProvideScaffoldPadding(padding = paddingValues) {
            WebView(
                state = webViewState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding()),
                onCreated = {
                    it.settings.javaScriptEnabled = true
                }
            )
        }
    }
}

@Composable
private fun CopyUrlIcon(onClick: () -> Unit) {
    IconButton(
        imageVector = Icons.Copy,
        onClick = onClick,
        tint = MaterialTheme.colorScheme.primary
    )
}

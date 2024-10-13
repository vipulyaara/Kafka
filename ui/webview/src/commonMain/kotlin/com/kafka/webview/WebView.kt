@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.webview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import com.kafka.base.debug
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.scaffoldPadding
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.rememberWebViewState

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
//                factory = null,
//                onCreated = {
//                    it.settings.javaScriptEnabled = true
//                }
//                onCreated = {
////                    it.settings.javaScriptEnabled = true
//                }
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

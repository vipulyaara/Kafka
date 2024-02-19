package com.kafka.reader.online

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.WebViewState
import kotlinx.coroutines.flow.MutableStateFlow
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.material.CloseButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.topScaffoldPadding
import com.google.accompanist.web.WebView as AccompanistWebView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun OnlineReader(viewModel: OnlineReaderViewModel = hiltViewModel()) {
    val webViewState by viewModel.webViewState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier,
        topBar = {
            if (webViewState != null) {
                TopBar(webViewState = webViewState!!, viewModel = viewModel)
            }
        }
    ) { paddingValues ->
        ProvideScaffoldPadding(padding = paddingValues) {
            if (webViewState != null) {
                WebView(webViewState = webViewState!!, urlState = viewModel.urlState)
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebView(
    webViewState: WebViewState,
    urlState: MutableStateFlow<String>,
    modifier: Modifier = Modifier
) {
    AccompanistWebView(
        state = webViewState,
        modifier = modifier
            .fillMaxSize()
            .padding(top = topScaffoldPadding())
            .navigationBarsPadding(),
        onCreated = {
            it.settings.javaScriptEnabled = true
        },
        client = remember { ReaderWebViewClient(urlState) }
    )
}

@Composable
private fun TopBar(webViewState: WebViewState, viewModel: OnlineReaderViewModel) {
    val context = LocalContext.current

    Column {
        TopBar(
            title = webViewState.pageTitle.orEmpty(),
            navigationIcon = { CloseButton(onClick = viewModel::goBack) },
            actions = {
                if (webViewState.lastLoadedUrl != null) {
                    ShareUrlIcon { viewModel.shareItem(context = context) }
                }
            }
        )

        AnimatedVisibility(webViewState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun ShareUrlIcon(onClick: () -> Unit) {
    IconButton(
        imageVector = Icons.Share,
        onClick = onClick,
        tint = MaterialTheme.colorScheme.primary
    )
}

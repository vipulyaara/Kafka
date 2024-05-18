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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    val readerState by viewModel.readerState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val topAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    var scrolledY by remember { mutableFloatStateOf(0f) }
    var isTopAppBarVisible by remember { mutableStateOf(true) }

    fun updateTopAppBarVisibility(dy: Float) {
        val newScrollY = scrolledY + dy
        scrolledY = newScrollY
        isTopAppBarVisible = newScrollY > 0f // Hide when scrolled down
    }

//    val nestedScrollModifier = Modifier
//        .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
//        .verticalScroll { dy ->
//            updateTopAppBarVisibility(dy)
//        }

    Scaffold(
        topBar = {
            if (webViewState != null) {
                TopBar(
                    webViewState = webViewState!!,
                    isDownloadAvailable = readerState?.fileId != null,
                    downloadItem = { viewModel.downloadItem(context) },
                    shareItem = { viewModel.shareItem(context) },
                    goBack = viewModel::goBack
                )
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
private fun TopBar(
    webViewState: WebViewState,
    isDownloadAvailable: Boolean,
    downloadItem: () -> Unit,
    shareItem: () -> Unit,
    goBack: () -> Unit
) {
    Column {
        TopBar(
            title = webViewState.pageTitle.orEmpty(),
            navigationIcon = { CloseButton(onClick = goBack) },
            actions = {
                if (isDownloadAvailable) {
                    DownloadIcon(onClick = downloadItem)
                }
                if (webViewState.lastLoadedUrl != null) {
                    ShareUrlIcon(onClick = shareItem)
                }
            }
        )

        AnimatedVisibility(webViewState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun DownloadIcon(onClick: () -> Unit) {
    IconButton(
        imageVector = Icons.Download,
        onClick = onClick,
        tint = MaterialTheme.colorScheme.primary
    )
}

@Composable
private fun ShareUrlIcon(onClick: () -> Unit) {
    IconButton(
        imageVector = Icons.Share,
        onClick = onClick,
        tint = MaterialTheme.colorScheme.primary
    )
}

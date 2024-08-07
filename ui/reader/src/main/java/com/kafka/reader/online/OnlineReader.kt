@file:Suppress("DEPRECATION")

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
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.WebViewState
import com.kafka.data.feature.item.DownloadInfo
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.feature.item.ItemWithDownload
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.R
import org.kafka.ui.components.file.DownloadStatusIcons
import org.kafka.ui.components.material.CloseButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.topScaffoldPadding
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.ui.downloader.LocalDownloader
import com.google.accompanist.web.WebView as AccompanistWebView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun OnlineReader(
    viewModel: OnlineReaderViewModel = hiltViewModel(),
    openOfflineReader: (String) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val webViewState by viewModel.webViewState.collectAsStateWithLifecycle()
    val fileId = state.readerState?.fileId.orEmpty()

    Scaffold(
        topBar = {
            if (webViewState != null) {
                TopBar(
                    webViewState = webViewState!!,
                    fileId = fileId,
                    download = state.download,
                    openOfflineReader = {
                        viewModel.logOpenOfflineReader()
                        openOfflineReader(fileId)
                    },
                    downloadItem = { viewModel.downloadItem(fileId, context) },
                    shareItem = { viewModel.shareItem(context) },
                    goBack = { viewModel.goBack() }
                )
            }
        }
    ) { paddingValues ->
        ProvideScaffoldPadding(padding = paddingValues) {
            if (webViewState != null) {
                WebView(webViewState = webViewState!!, updateUrl = viewModel::updateUrl)
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebView(
    webViewState: WebViewState,
    updateUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AccompanistWebView(
        state = webViewState,
        modifier = modifier
            .fillMaxSize()
            .padding(top = topScaffoldPadding())
            .navigationBarsPadding(),
        onCreated = { it.settings.javaScriptEnabled = true },
        client = remember { ReaderWebViewClient(updateUrl) }
    )
}

@Composable
private fun TopBar(
    webViewState: WebViewState,
    fileId: String?,
    download: ItemWithDownload?,
    shareItem: () -> Unit,
    openOfflineReader: () -> Unit,
    downloadItem: () -> Unit,
    goBack: () -> Unit,
) {
    Column {
        TopBar(
            title = webViewState.pageTitle.orEmpty(),
            navigationIcon = { CloseButton(onClick = goBack) },
            actions = {
                if (webViewState.lastLoadedUrl != null) {
                    IconButton(
                        tint = MaterialTheme.colorScheme.primary,
                        imageVector = Icons.Share,
                        onClick = shareItem,
                    )
                }

                if (fileId != null) {
                    DownloadIcon(
                        fileId = fileId,
                        downloadInfo = download?.downloadInfo,
                        onDownloadClicked = downloadItem,
                        openOfflineReader = openOfflineReader
                    )
                }
            }
        )

        AnimatedVisibility(webViewState.isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun DownloadIcon(
    fileId: String,
    downloadInfo: DownloadInfo?,
    downloader: Downloader = LocalDownloader.current,
    onDownloadClicked: () -> Unit,
    openOfflineReader: () -> Unit,
) {
    if (downloadInfo?.status == DownloadStatus.COMPLETED) {
        IconButton(
            imageVector = Icons.ReadOffline,
            tint = MaterialTheme.colorScheme.primary,
            onClick = openOfflineReader,
        )
    } else if (downloadInfo != null) {
        DownloadStatusIcons(
            downloadInfo = downloadInfo
        )
    } else {
        IconButton(
            imageVector = Icons.Download,
            contentDescription = stringResource(R.string.cd_download_file),
            onClick = onDownloadClicked
        )
    }
}

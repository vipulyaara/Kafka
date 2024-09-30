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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.WebContent
import com.google.accompanist.web.WebViewState
import com.kafka.data.feature.item.DownloadInfo
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.feature.item.ItemWithDownload
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.reader.R.string
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.R
import org.kafka.ui.components.file.DownloadStatusIcons
import org.kafka.ui.components.material.AlertDialog
import org.kafka.ui.components.material.AlertDialogAction
import org.kafka.ui.components.material.CloseButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.topScaffoldPadding
import com.google.accompanist.web.WebView as AccompanistWebView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun OnlineReader(
    viewModel: OnlineReaderViewModel,
    openOfflineReader: (String) -> Unit,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()
    val fileId = state.readerState?.fileId.orEmpty()
    val readerState by viewModel.readerState.collectAsStateWithLifecycle()
    val webViewState by remember(readerState?.url) {
        mutableStateOf(readerState?.url?.let { WebViewState(WebContent.Url(url = it)) })
    }

    LaunchedEffect(fileId, state.autoDownload) {
        if (state.autoDownload) {
            viewModel.downloadItem(fileId)
        }
    }

    LaunchedEffect(state.download) {
        if (state.download?.downloadInfo?.status == DownloadStatus.COMPLETED) {
            viewModel.showDownloadComplete.value = true
        }
    }

    val downloadComplete by viewModel.showDownloadComplete
    if (downloadComplete) {
        DownloadCompleteDialog(
            openOfflineReader = {
                viewModel.showDownloadComplete.value = false
                openOfflineReader(fileId)
            },
            onDismiss = { viewModel.showDownloadComplete.value = false }
        )
    }

    Scaffold(
        topBar = {
            if (webViewState != null) {
                TopBar(
                    webViewState = webViewState!!,
                    showDownloadIcon = state.showDownloadIcon,
                    fileId = fileId,
                    download = state.download,
                    openOfflineReader = {
                        viewModel.logOpenOfflineReader()
                        openOfflineReader(fileId)
                    },
                    downloadItem = { viewModel.downloadItem(fileId) },
                    shareItem = { viewModel.shareItem(context) },
                    goBack = { viewModel.goBack() }
                )
            }
        }
    ) { paddingValues ->
        ProvideScaffoldPadding(padding = paddingValues) {
            if (webViewState != null) {
                WebView(webViewState = webViewState!!, updateUrl = viewModel::updateCurrentPage)
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
    showDownloadIcon: Boolean,
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

                if (fileId != null && showDownloadIcon) {
                    DownloadIcon(
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
    downloadInfo: DownloadInfo?,
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
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(R.string.cd_download_file),
            onClick = onDownloadClicked
        )
    }
}

@Composable
fun DownloadCompleteDialog(openOfflineReader: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        title = stringResource(string.download_complete),
        text = stringResource(string.continue_reading_offline),
        confirmButton = {
            AlertDialogAction(
                text = stringResource(string.open_offline),
                onClick = openOfflineReader
            )
        },
        cancelButton = {
            AlertDialogAction(text = stringResource(string.close), onClick = onDismiss)
        },
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}
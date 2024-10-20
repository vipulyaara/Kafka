@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.online

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.getContext
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.data.feature.item.DownloadInfo
import com.kafka.data.feature.item.DownloadStatus
import com.kafka.data.feature.item.ItemWithDownload
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.item.DownloadStatusIcons
import com.kafka.ui.components.material.AlertDialog
import com.kafka.ui.components.material.AlertDialogAction
import com.kafka.ui.components.material.CloseButton
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.topScaffoldPadding
import com.multiplatform.webview.web.WebContent
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import kafka.ui.reader.online.generated.resources.Res
import kafka.ui.reader.online.generated.resources.cd_download_file
import kafka.ui.reader.online.generated.resources.close
import kafka.ui.reader.online.generated.resources.continue_reading_offline
import kafka.ui.reader.online.generated.resources.download_complete
import kafka.ui.reader.online.generated.resources.open_offline
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnlineReader(viewModel: OnlineReaderViewModel, openOfflineReader: (String) -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val fileId = state.readerState?.fileId.orEmpty()
    val readerState by viewModel.readerState.collectAsStateWithLifecycle()
    val webViewState by remember(readerState?.url) {
        mutableStateOf(readerState?.url?.let { WebViewState(WebContent.Url(url = it)) })
    }

    val context = getContext()

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

@Composable
private fun WebView(
    webViewState: WebViewState,
    updateUrl: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(webViewState.lastLoadedUrl) {
        updateUrl(webViewState.lastLoadedUrl.orEmpty())
    }

    WebView(
        state = webViewState,
        modifier = modifier
            .fillMaxSize()
            .padding(top = topScaffoldPadding())
            .navigationBarsPadding()
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
        DownloadStatusIcons(downloadInfo = downloadInfo)
    } else {
        IconButton(
            imageVector = Icons.Download,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(Res.string.cd_download_file),
            onClick = onDownloadClicked
        )
    }
}

@Composable
fun DownloadCompleteDialog(openOfflineReader: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        title = stringResource(Res.string.download_complete),
        text = stringResource(Res.string.continue_reading_offline),
        confirmButton = {
            AlertDialogAction(
                text = stringResource(Res.string.open_offline),
                onClick = openOfflineReader
            )
        },
        cancelButton = {
            AlertDialogAction(text = stringResource(Res.string.close), onClick = onDismiss)
        },
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

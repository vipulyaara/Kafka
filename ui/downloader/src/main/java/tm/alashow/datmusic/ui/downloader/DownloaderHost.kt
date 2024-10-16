/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.ui.downloader

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.kafka.ui.downloader.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.downloader.DownloaderEvent

@Composable
actual fun DownloaderHost(
    downloader: Downloader,
    content: @Composable () -> Unit,
) {
    var downloadsLocationDialogShown by remember { mutableStateOf(false) }
    CollectEvent(downloader.downloaderEvents) { event ->
        when (event) {
            DownloaderEvent.ChooseDownloadsLocation -> {
                downloadsLocationDialogShown = true
            }

            else -> Unit
        }
    }

    CompositionLocalProvider(LocalDownloader provides downloader) {
        DownloadsLocationDialog(
            dialogShown = downloadsLocationDialogShown,
            onDismiss = { downloadsLocationDialogShown = false }
        )
        content()
    }
}

@Composable
private fun DownloadsLocationDialog(
    dialogShown: Boolean,
    downloader: Downloader = LocalDownloader.current,
    onDismiss: () -> Unit,
) {
    val coroutine = rememberCoroutineScope()
    val documentTreeLauncher =
        rememberLauncherForActivityResult(contract = WriteableOpenDocumentTree()) {
            coroutine.launch {
                try {
                    downloader.setDownloadsLocation(it.toString())
                } catch (e: Exception) {
                    Log.e("DownloaderHost", "${e.localizedMessage} download location failed")
                }
            }
        }

    if (dialogShown) {
        AlertDialog(
            properties = DialogProperties(usePlatformDefaultWidth = true),
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(R.string.downloader_downloadsLocationSelect_title)) },
            text = { Text(stringResource(R.string.downloader_downloadsLocationSelect_text)) },
            dismissButton = {
                Button(
                    onClick = {
                        onDismiss()
                        documentTreeLauncher.launch(null)
                    },
                ) {
                    Text(stringResource(R.string.downloader_downloadsLocationSelect_next))
                }
            },
            confirmButton = {},
        )
    }
}

@Composable
fun <T> CollectEvent(
    flow: Flow<T>,
    lifecycle: Lifecycle = LocalLifecycleOwner.current.lifecycle,
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED,
    collector: (T) -> Unit,
): Unit = LaunchedEffect(lifecycle, flow) {
    lifecycle.repeatOnLifecycle(minActiveState) {
        flow.collect {
            collector(it)
        }
    }
}

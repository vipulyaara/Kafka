package org.kafka.item.files

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.File
import com.kafka.data.entities.isAudio
import com.kafka.data.feature.item.DownloadInfo
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.models.LocalPlaybackConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kafka.common.extensions.AnimatedVisibility
import org.kafka.common.extensions.elevation
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconResource
import org.kafka.navigation.LeafScreen.Reader
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.navigation.RootScreen
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.file.DownloadStatusIcons
import org.kafka.ui.components.material.TopBar
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.ui.downloader.LocalDownloader
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary

@Composable
fun Files(viewModel: FilesViewModel = hiltViewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()
    val playbackConnection = LocalPlaybackConnection.current
    val lazyListState = rememberLazyListState()

    Scaffold(topBar = { TopBar(viewState.title, lazyListState = lazyListState) }) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Files(padding, viewState, navigator, currentRoot, lazyListState, playbackConnection)
        }
    }
}

@Composable
private fun Files(
    padding: PaddingValues,
    viewState: FilesViewState,
    navigator: Navigator,
    currentRoot: RootScreen,
    lazyListState: LazyListState = rememberLazyListState(),
    playbackConnection: PlaybackConnection = LocalPlaybackConnection.current
) {
    val downloader: Downloader = LocalDownloader.current
    val scope: CoroutineScope = rememberCoroutineScope()

    val onFileClicked: (File) -> Unit = {
        if (it.isAudio()) {
            playbackConnection.playAudio(it.asAudio())
        } else {
            navigator.navigate(Reader.buildRoute(it.fileId, currentRoot))
        }
    }

    LazyColumn(modifier = Modifier, state = lazyListState, contentPadding = padding) {
        items(viewState.files, key = { it.fileId }) { file ->
            val download = viewState.downloads.firstOrNull { it.file.fileId == file.fileId }

            File(
                file = file,
                onFileClicked = onFileClicked,
                downloadInfo = download?.downloadInfo,
                downloader = downloader,
                scope = scope
            )
        }
    }
}

@Composable
private fun File(
    file: File,
    onFileClicked: (File) -> Unit,
    downloadInfo: DownloadInfo?,
    downloader: Downloader = LocalDownloader.current,
    scope: CoroutineScope = rememberCoroutineScope()
) {

    val fileSubtitle = if (downloadInfo?.status?.isActive() == true) {
        listOf(file.extension, downloadInfo.sizeStatus).joinToString(" - ")
    } else {
        listOf(file.extension, file.mapSize()).joinToString(" - ")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFileClicked(file) }
            .animateContentSize()
            .padding(Dimens.Spacing16),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing16),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .animateContentSize()
        ) {
            Text(
                text = file.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textPrimary,
                modifier = Modifier
            )

            Text(
                text = fileSubtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
            )
        }

        Box {
            AnimatedVisibility(downloadInfo == null) {
                IconButton(onClick = { scope.launch { downloader.enqueueFile(file.fileId) } }) {
                    IconResource(
                        imageVector = Icons.Download,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(downloadInfo != null) {
                DownloadStatusIcons(
                    downloadInfo = downloadInfo!!,
                    downloader = downloader,
                    scope = scope
                )
            }
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    lazyListState: LazyListState = rememberLazyListState(),
    navigator: Navigator = LocalNavigator.current
) {
    TopBar(
        title = title,
        navigationIcon = {
            IconButton(onClick = { navigator.goBack() }) {
                IconResource(imageVector = Icons.Back, tint = MaterialTheme.colorScheme.primary)
            }
        },
        elevation = remember { lazyListState.elevation }
    )
}

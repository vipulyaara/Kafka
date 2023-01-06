package org.kafka.item.files

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.File
import com.kafka.data.entities.isAudio
import com.sarahang.playback.core.models.LocalPlaybackConnection
import org.kafka.common.Icons
import org.kafka.common.extensions.elevation
import org.kafka.common.widgets.IconResource
import org.kafka.navigation.LeafScreen.Reader
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.ui.components.material.TopBar
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary

@Composable
fun Files(viewModel: FilesViewModel = hiltViewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val navigator = LocalNavigator.current
    val currentRoot by navigator.currentRoot.collectAsStateWithLifecycle()
    val playbackConnection = LocalPlaybackConnection.current

    Scaffold(topBar = { TopBar() }) { padding ->
        LazyColumn(modifier = Modifier, contentPadding = padding) {
            items(viewState.files, key = { it.fileId }) { file ->
                File(
                    file = file,
                    startDownload = { viewModel.downloadFile(file.fileId) },
                    openReader = {
                        navigator.navigate(
                            Reader.buildRoute(
                                viewModel.downloadState.value.toString(),
                                currentRoot
                            )
                        )
                    },
                    playAudio = {
                        playbackConnection.playAudio(file.asAudio())
                    }
                )
            }
        }
    }
}

@Composable
private fun File(
    file: File,
    startDownload: () -> Unit,
    openReader: () -> Unit,
    playAudio: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (file.isAudio()) {
                    playAudio()
                } else {
                    openReader()
                }
            }
            .padding(Dimens.Spacing16),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.title.orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textPrimary,
                modifier = Modifier
            )

            Text(
                text = file.extension + " - " + file.size.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
            )
        }

        IconButton(onClick = startDownload) {
            IconResource(imageVector = Icons.Download, tint = MaterialTheme.colorScheme.primary)
        }
    }

}

@Composable
private fun TopBar(
    lazyListState: LazyListState = rememberLazyListState(),
    navigator: Navigator = LocalNavigator.current
) {
    TopBar(
        navigationIcon = {
            IconButton(onClick = { navigator.goBack() }) {
                IconResource(imageVector = Icons.Back, tint = MaterialTheme.colorScheme.primary)
            }
        },
        elevation = remember { lazyListState.elevation }
    )
}

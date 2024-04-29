package org.kafka.item.files

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.File
import kotlinx.coroutines.CoroutineScope
import org.kafka.common.elevation
import org.kafka.common.test.testTagUi
import org.kafka.common.widgets.shadowMaterial
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.Navigator
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.material.BackButton
import org.kafka.ui.components.material.TopBar
import org.kafka.ui.components.scaffoldPadding
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.ui.downloader.LocalDownloader

@Composable
fun Files(viewModel: FilesViewModel = hiltViewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    Scaffold(
        modifier = Modifier.testTagUi("download_files_screen"),
        topBar = { TopBar(viewState.title, lazyListState = lazyListState) }) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Files(
                viewState = viewState,
                selectedExtension = viewModel.selectedExtension,
                selectExtension = { viewModel.selectedExtension = it },
                onFileClicked = viewModel::onFileClicked,
                onDownloadClicked = viewModel::onDownloadClicked,
                lazyListState = lazyListState
            )
        }
    }
}

@Composable
private fun Files(
    viewState: FilesViewState,
    selectedExtension: String?,
    selectExtension: (String?) -> Unit,
    onFileClicked: (File) -> Unit,
    onDownloadClicked: (File) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val downloader: Downloader = LocalDownloader.current
    val scope: CoroutineScope = rememberCoroutineScope()
    val itemsCount = remember(viewState.filteredFiles) { viewState.filteredFiles.size }

    LazyColumn(state = lazyListState, contentPadding = scaffoldPadding()) {
        stickyHeader {
            ExtensionFilter(
                actionLabels = viewState.actionLabels,
                selectedFilter = selectedExtension,
                itemsCount = itemsCount,
                onItemSelected = { selectExtension(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(items = viewState.filteredFiles, key = { it.fileId }) { file ->
            val download by remember(viewState.downloads, file) {
                derivedStateOf {
                    viewState.downloads.firstOrNull { it.file.fileId == file.fileId }
                }
            }

            FileItem(
                file = file,
                onFileClicked = onFileClicked,
                onDownloadClicked = onDownloadClicked,
                downloadInfo = download?.downloadInfo,
                modifier = Modifier.animateItemPlacement(),
                downloader = downloader,
                scope = scope
            )
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
        navigationIcon = { BackButton { navigator.goBack() } },
        modifier = Modifier.shadowMaterial(lazyListState.elevation)
    )
}

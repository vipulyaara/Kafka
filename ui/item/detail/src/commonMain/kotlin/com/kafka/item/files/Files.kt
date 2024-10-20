@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

package com.kafka.item.files

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.elevation
import com.kafka.common.widgets.shadowMaterial
import com.kafka.data.entities.File
import com.kafka.navigation.LocalNavigator
import com.kafka.navigation.Navigator
import com.kafka.ui.components.MessageBox
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.BackButton
import com.kafka.ui.components.material.TopBar
import com.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun Files(viewModel: FilesViewModel) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val lazyListState = rememberLazyListState()

    Scaffold(
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

        if (viewState.downloadsWarningMessage.isNotEmpty()) {
            item {
                MessageBox(
                    text = viewState.downloadsWarningMessage,
                    modifier = Modifier.padding(Dimens.Spacing16)
                )
            }
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
                modifier = Modifier.animateItem()
            )
        }
    }
}

@Composable
private fun TopBar(
    title: String,
    lazyListState: LazyListState = rememberLazyListState(),
    navigator: Navigator = LocalNavigator.current,
) {
    TopBar(
        title = title,
        navigationIcon = { BackButton { navigator.goBack() } },
        modifier = Modifier.shadowMaterial(lazyListState.elevation)
    )
}

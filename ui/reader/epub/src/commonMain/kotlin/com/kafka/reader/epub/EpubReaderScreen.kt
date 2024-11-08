@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.reader.epub.components.TocComponent
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.progress.InfiniteProgressBar
import ui.common.theme.theme.AppTheme
import ui.common.theme.theme.Dimens

@Composable
fun EpubReader(viewModel: EpubReaderViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val settings by viewModel.readerSettings.collectAsStateWithLifecycle()

    AppTheme(isDarkTheme = settings.isDarkMode) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                ReaderTopBar(
                    scrollBehavior = scrollBehavior,
                    viewModel = viewModel,
                    containerColor = settings.backgroundColor
                )
            }
        ) {
            ProvideScaffoldPadding(it) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (state.epubBook != null) {
                        EpubBook(
                            ebook = state.epubBook!!,
                            settings = settings,
                            lazyListState = viewModel.lazyListState,
                            navigate = viewModel::navigate,
                            changeSettings = viewModel::updateSettings
                        )
                        TocComponent(
                            show = viewModel.showTocSheet,
                            chapters = state.epubBook!!.chapters,
                            selectChapter = viewModel::navigate
                        )
                    } else {
                        if (state.loading) {
                            LoadingWithProgress(
                                progress = state.progress,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingWithProgress(progress: String?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        InfiniteProgressBar()
        if (progress != null) {
            Text(
                text = progress,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

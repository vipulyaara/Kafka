@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.extensions.getContext
import com.kafka.reader.epub.components.TocSheet
import com.kafka.reader.epub.components.rememberSettingsState
import com.kafka.reader.epub.components.rememberTocState
import com.kafka.reader.epub.settings.theme
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.progress.InfiniteProgressBar
import kotlinx.coroutines.launch
import ui.common.theme.theme.Dimens

@Composable
fun ReaderScreen(viewModel: ReaderViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()
    val settingsState = rememberSettingsState()
    val tocState = rememberTocState()
    val context = getContext()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            ReaderTopBar(
                scrollBehavior = scrollBehavior,
                settingsState = settingsState,
                tocState = tocState,
                theme = state.settings.theme,
                actionsButtonVisible = state.epubBook != null,
                shareItem = { viewModel.shareItemText(context) }
            )
        }
    ) {
        ProvideScaffoldPadding(it) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.epubBook != null) {
                    val pagerState = rememberPagerState(initialPage = state.epubBook!!.lastSeenPage) { state.epubBook!!.chapters.size }

                    LaunchedEffect(pagerState) {
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            viewModel.onPageChanged(page)
                        }
                    }

                    EpubBook(
                        readerState = state,
                        settingsState = settingsState,
                        pagerState = pagerState,
                        onPageScrolled = viewModel::onPageScrolled,
                        navigate = viewModel::navigate,
                        changeSettings = viewModel::updateSettings
                    )

                    TocSheet(
                        tocState = tocState,
                        chapters = state.epubBook!!.chapters,
                        selectChapter = { chapterId ->
                            coroutineScope.launch {
                                val index = state.epubBook!!.chapters
                                    .indexOfFirst { it.chapterId == chapterId }
                                pagerState.scrollToPage(index)
                            }
                        }
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

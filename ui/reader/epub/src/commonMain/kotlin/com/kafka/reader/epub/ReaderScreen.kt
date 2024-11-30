@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateMap
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
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.EpubBook
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
                    val pagerState =
                        rememberPagerState(initialPage = state.epubBook!!.lastSeenPage) { state.epubBook!!.chapters.size }
                    val pagesListStates =
                        remember<SnapshotStateMap<Int, LazyListState>> { mutableStateMapOf() }

                    LaunchedEffect(pagerState) {
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            viewModel.onPageChanged(page)
                        }
                    }

                    ReaderContent(
                        readerState = state,
                        settingsState = settingsState,
                        pagerState = pagerState,
                        highlights = state.highlights,
                        onPageScrolled = viewModel::onPageScrolled,
                        onHighlight = viewModel::addHighlight,
                        navigate = viewModel::navigate,
                        changeSettings = viewModel::updateSettings,
                        pagesListStates = pagesListStates
                    )

                    TocSheet(
                        tocState = tocState,
                        navPoints = state.epubBook!!.navPoints,
                        onNavPointClicked = { navPointSrc ->
                            coroutineScope.launch {
                                navigateToPageAndAnchor(
                                    epubBook = state.epubBook!!,
                                    navPointSrc = navPointSrc,
                                    pagerState = pagerState,
                                    pagesListStates = pagesListStates
                                )
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

private suspend fun navigateToPageAndAnchor(
    epubBook: EpubBook,
    navPointSrc: String,
    pagerState: PagerState,
    pagesListStates: SnapshotStateMap<Int, LazyListState>
) {
    val pageIndex = epubBook.chapters
        .indexOfFirst {
            it.absPath.endsWith(
                navPointSrc.substringBefore("#")
            )
        }

    val fragmentIndex = epubBook.chapters[pageIndex].contentElements
        .indexOfFirst {
            (it as? ContentElement.Anchor)?.id == navPointSrc.substringAfter("#")
        }
        .coerceAtLeast(0)

    pagerState.animateScrollToPage(pageIndex)
    pagesListStates[pageIndex]?.animateScrollToItem(fragmentIndex)
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

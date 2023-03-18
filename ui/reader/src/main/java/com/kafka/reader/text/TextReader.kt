package com.kafka.reader.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentItem
import com.kafka.data.entities.RecentTextItem
import com.kafka.reader.controls.GoToPage
import kotlinx.coroutines.launch
import org.kafka.common.simpleClickable
import org.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
internal fun TextReader(
    fileId: String,
    modifier: Modifier = Modifier,
    viewModel: TextReaderViewModel = hiltViewModel()
) {
    val viewState by viewModel.readerState.collectAsStateWithLifecycle()

    LaunchedEffect(fileId) {
        viewModel.observeTextFile(fileId)
    }

    viewState.recentItem?.let {
        val lazyListState = rememberLazyListState(it.currentPage)
        val currentPage by remember { derivedStateOf { lazyListState.firstVisibleItemIndex } }

        LaunchedEffect(it, currentPage) {
            viewModel.onPageChanged(fileId, currentPage)
        }

        TextReader(modifier, viewModel, it, currentPage, lazyListState)
    }
}

@Composable
private fun TextReader(
    modifier: Modifier,
    viewModel: TextReaderViewModel,
    recentTextItem: RecentTextItem,
    currentPage: Int = 0,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val scope = rememberCoroutineScope()
    Surface(modifier = modifier) {
        val scaffoldPadding = scaffoldPadding()

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(contentPadding = scaffoldPadding, state = lazyListState) {
                items(recentTextItem.pages, key = { it.index }) { page ->
                    Page(
                        page = page,
                        modifier = Modifier.simpleClickable { viewModel.toggleControls() }
                    )
                }
            }

            GoToPage(
                currentPage = currentPage,
                goToPage = {
                    scope.launch { lazyListState.animateScrollToItem(it) }
                    viewModel.showControls = false
                },
                showControls = viewModel.showControls
            )
        }
    }
}

@Composable
private fun Page(page: RecentTextItem.Page, modifier: Modifier = Modifier) {
    SelectionContainer {
        Box(modifier = modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.padding(Dimens.Spacing24),
                text = page.text,
                textAlign = TextAlign.Justify,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

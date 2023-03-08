package com.kafka.reader.text

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.data.entities.RecentItem
import com.kafka.reader.controls.GoToPage
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

    LaunchedEffect(fileId, viewModel.currentPage) {
        viewModel.onPageChanged(fileId, viewModel.currentPage)
    }

    viewState.recentItem?.let { TextReader(modifier, viewModel, it) }
}

@Composable
private fun TextReader(
    modifier: Modifier,
    viewModel: TextReaderViewModel,
    recentTextItem: RecentItem.Readable
) {
    Surface(modifier = modifier) {
        val scaffoldPadding = scaffoldPadding()

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(contentPadding = scaffoldPadding, state = viewModel.lazyListState) {
                items(recentTextItem.pages, key = { it.index }) { page ->
                    Page(
                        page = page,
                        modifier = Modifier.simpleClickable { viewModel.toggleControls() }
                    )
                }
            }

            GoToPage(
                currentPage = viewModel.currentPage,
                goToPage = viewModel::goToPage,
                showControls = viewModel.showControls
            )
        }
    }
}

@Composable
private fun Page(page: RecentItem.Readable.Page, modifier: Modifier = Modifier) {
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

package com.kafka.reader.epub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kafka.common.extensions.rememberMutableState
import com.kafka.common.simpleClickable
import com.kafka.reader.epub.models.EpubBook
import com.kafka.reader.epub.models.EpubChapter
import com.kafka.reader.epub.parser.BookTextMapper
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.ui.SettingsSheet
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun EpubReader(viewModel: EpubReaderViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.epubBook != null) {
            EpubBook(
                ebook = state.epubBook!!,
                chapters = state.epubBook!!.chapters,
                lazyListState = viewModel.lazyListState
            )
        } else {
            if (state.loading) {
                InfiniteProgressBar(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
private fun EpubBook(ebook: EpubBook, chapters: List<EpubChapter>, lazyListState: LazyListState) {
    val readerSettings = ReaderSettings.Default
    var settings by remember { mutableStateOf(readerSettings) }
    var showSettings by rememberMutableState { false }

    if (showSettings) {
        SettingsSheet(
            settings = settings,
            onDismiss = { showSettings = false },
            changeSettings = { settings = it }
        )
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .background(settings.background.color)
            .simpleClickable { showSettings = !showSettings },
        contentPadding = scaffoldPadding()
    ) {
        items(chapters) { chapter ->
            Chapter(ebook = ebook, chapter = chapter, settings = settings)
        }
    }
}

@Composable
private fun Chapter(ebook: EpubBook, chapter: EpubChapter, settings: ReaderSettings) {
    val paragraphs = remember { chunkText(chapter.body) }
    paragraphs.forEach { para ->
        val imgEntry = BookTextMapper.ImgEntry.fromXMLString(para)

        if (imgEntry == null) {
            SelectionContainer {
                Text(
                    text = para,
                    modifier = Modifier.padding(Dimens.Spacing16),
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = settings.fontStyle.fontFamily,
                    fontSize = settings.fontSize.fontSize,
                    lineHeight = settings.fontSize.lineHeight,
                    textAlign = TextAlign.Justify
                )
            }
        } else {
            val image = ebook.images.find { it.absPath == imgEntry.path }
            image?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(image.image)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 6.dp)
                )
            }
        }
    }
}

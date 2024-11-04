@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kafka.common.extensions.rememberMutableState
import com.kafka.common.simpleClickable
import com.kafka.reader.epub.components.CodeBlockElement
import com.kafka.reader.epub.components.HeadingElement
import com.kafka.reader.epub.components.ListElement
import com.kafka.reader.epub.components.QuoteElement
import com.kafka.reader.epub.components.TextElement
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.SettingsSheet
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.kafka.ui.components.scaffoldPadding
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.EpubBook
import kafka.reader.core.models.EpubChapter
import ui.common.theme.theme.Dimens

@Composable
fun EpubReader(viewModel: EpubReaderViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { ReaderTopBar(scrollBehavior = scrollBehavior, viewModel = viewModel) }
    ) {
        ProvideScaffoldPadding(it) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (state.epubBook != null) {
                    EpubBook(ebook = state.epubBook!!)
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
private fun EpubBook(ebook: EpubBook) {
    val chapters = ebook.chapters
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
        modifier = Modifier
            .fillMaxWidth()
            .background(settings.background.color)
            .simpleClickable { showSettings = !showSettings },
        contentPadding = scaffoldPadding()
    ) {
        items(chapters) { page ->
            Chapter(ebook = ebook, chapter = page, settings = settings)
        }
    }
}

@Composable
private fun Chapter(ebook: EpubBook, chapter: EpubChapter, settings: ReaderSettings) {
    SelectionContainer {
        Column {
            chapter.contentElements.forEach { element ->
                when (element) {
                    is ContentElement.Text -> TextElement(element, settings)
                    is ContentElement.Heading -> HeadingElement(element, settings)
                    is ContentElement.Quote -> QuoteElement(element, settings)
                    is ContentElement.List -> ListElement(element, settings)
                    is ContentElement.CodeBlock -> CodeBlockElement(element, settings)
                    is ContentElement.Image -> {
                        val image = ebook.images.find { it.absPath == element.path }
                        image?.let {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalPlatformContext.current)
                                    .data(image.image)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = element.caption,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            )
                        }
                    }

                    is ContentElement.Table -> {
                        // TODO: Implement table rendering
                    }

                    is ContentElement.Divider -> {
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                .padding(vertical = 16.dp)
                        )
                    }

                    else -> {

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

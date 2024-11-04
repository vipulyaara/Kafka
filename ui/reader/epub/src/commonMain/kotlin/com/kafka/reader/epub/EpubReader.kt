@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kafka.common.extensions.rememberMutableState
import com.kafka.reader.epub.components.CodeBlockElement
import com.kafka.reader.epub.components.HeadingElement
import com.kafka.reader.epub.components.ListElement
import com.kafka.reader.epub.components.QuoteElement
import com.kafka.reader.epub.components.TableComponent
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

    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = Modifier
            .fillMaxWidth()
            .background(settings.background.color)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down: PointerInputChange = awaitFirstDown()
                    val up: PointerInputChange? = waitForUpOrCancellation()
                    // only trigger the click if the pointer hasn't moved up or down
                    // i.e only on tap gesture
                    if (up != null && down.id == up.id) {
                        showSettings = !showSettings
                    }
                }
            },
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
                    is ContentElement.Text -> {
                        TextElement(element = element, settings = settings)
                    }

                    is ContentElement.Heading -> {
                        HeadingElement(element = element, settings = settings)
                    }

                    is ContentElement.Quote -> {
                        QuoteElement(element = element, settings = settings)
                    }

                    is ContentElement.Listing -> {
                        ListElement(element = element, settings = settings)
                    }

                    is ContentElement.CodeBlock -> {
                        CodeBlockElement(element = element, settings = settings)
                    }

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
                                    .padding(vertical = 8.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }
                    }

                    is ContentElement.Table -> {
                        TableComponent(element = element, settings = settings)
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

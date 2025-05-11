package com.kafka.reader.epub

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.kafka.base.debug
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.extensions.rememberSavableMutableState
import com.kafka.common.plus
import com.kafka.reader.epub.components.CodeBlockElement
import com.kafka.reader.epub.components.HeadingElement
import com.kafka.reader.epub.components.ListElement
import com.kafka.reader.epub.components.QuoteElement
import com.kafka.reader.epub.components.SettingsState
import com.kafka.reader.epub.components.TableComponent
import com.kafka.reader.epub.components.TextElement
import com.kafka.reader.epub.context.ContextActions
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.theme
import com.kafka.ui.components.scaffoldPadding
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.EpubBook
import kafka.reader.core.models.TextHighlight
import kafka.reader.core.models.toInlineElements
import kafka.reader.core.parser.EpubCFIParser
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import ui.common.theme.theme.Dimens

@OptIn(FlowPreview::class)
@Composable
fun ReaderContent(
    readerState: ReaderState,
    settingsState: SettingsState,
    pagerState: PagerState,
    highlights: List<TextHighlight>,
    onPageScrolled: (Int) -> Unit,
    onHighlight: (TextHighlight) -> Unit,
    navigate: (String) -> Unit,
    pagesListStates: SnapshotStateMap<Int, LazyListState>
) {
    val book = readerState.epubBook!!
    val chapters = book.chapters
    val settings = readerState.settings
    var selectedText by rememberSavableMutableState<String?> { null }

    HorizontalPager(pagerState) { page ->
        val chapter = remember(chapters, page) { chapters[page] }
        val chapterHighlights = remember(highlights, chapter.chapterId) {
            highlights.filter { it.chapterId == chapter.chapterId }
        }
        val lazyListState = pagesListStates.getOrPut(page) {
            LazyListState(
                firstVisibleItemScrollOffset = if (book.lastSeenPage == page) {
                    book.lastPageOffset
                } else {
                    0
                }
            )
        }

        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.firstVisibleItemScrollOffset }
                .debounce(1000)
                .collect { index -> onPageScrolled(index) }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = lazyListState,
                contentPadding = scaffoldPadding() + PaddingValues(vertical = Dimens.Gutter) + PaddingValues(top = 100.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .background(settings.theme.backgroundColor)
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            val down: PointerInputChange = awaitFirstDown()
                            val up: PointerInputChange? = waitForUpOrCancellation()
                            if (up != null && down.id == up.id) {
                                settingsState.toggle()
                            }
                        }
                    },
            ) {
                debug { "Chapter: ${chapter.title} has ${chapter.contentElements.size} elements" }
                chapter.contentElements.forEach {
                    debug { "Element: $it" }
                }
                items(chapter.contentElements) { element ->
                    ReaderContent(
                        element = element,
                        settings = settings,
                        navigate = navigate,
                        ebook = book,
                        highlights = chapterHighlights,
                        toggleContextMenu = { text -> selectedText = text }
                    )
                }
            }

            AnimatedVisibilityFade(
                visible = selectedText != null,
                modifier = Modifier
                    .systemBarsPadding()
                    .padding(Dimens.Gutter)
                    .align(Alignment.BottomCenter),
            ) {
                ContextActions(
                    text = selectedText.orEmpty(),
                    isDarkTheme = settings.theme.isDarkTheme,
                    onHighlight = {},
                    onCopy = {},
                    onTranslate = {},
                    onAiExplain = {},
                    dismiss = { selectedText = null }
                )
            }
        }
    }
}

private fun createHighlight(
    bookId: String,
    chapterId: String,
    element: ContentElement.Text,
    startOffset: Int,
    endOffset: Int,
    color: TextHighlight.Color = TextHighlight.Color.BLUE
): TextHighlight {
    val cfi = EpubCFIParser.generate(
        chapterId = chapterId,
        elementPath = element.elementPath,
        startOffset = startOffset,
        endOffset = endOffset
    )

    return TextHighlight(
        bookId = bookId,
        chapterId = chapterId,
        text = element.content.substring(startOffset, endOffset),
        cfiRange = cfi,
        color = color.code
    )
}

@Composable
internal fun ReaderContent(
    element: ContentElement,
    settings: ReaderSettings,
    navigate: (String) -> Unit,
    ebook: EpubBook,
    highlights: List<TextHighlight>,
    toggleContextMenu: (String?) -> Unit,
) {
    when (element) {
        is ContentElement.Text -> {
            val elementHighlights = remember(highlights, element.elementPath) {
                highlights.filter { highlight ->
                    val location = EpubCFIParser.parse(highlight.cfiRange)
                    location?.elementPath == element.elementPath
                }
            }

            val mergedElement = remember(element, elementHighlights) {
                val highlightInlines = elementHighlights.toInlineElements()
                element.copy(
                    inlineElements = (element.inlineElements + highlightInlines)
                        .sortedBy { it.start }
                )
            }

            TextElement(
                element = mergedElement,
                settings = settings,
                navigate = navigate,
                toggleContextMenu = toggleContextMenu
            )
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
                    modifier = Modifier.padding(vertical = Dimens.Spacing08)
                )
            }
        }

        is ContentElement.Table -> {
            TableComponent(element = element, settings = settings, navigate = navigate)
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

        is ContentElement.Anchor -> {
            Spacer(modifier = Modifier.size(0.dp))
        }
    }
}

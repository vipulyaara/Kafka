package com.kafka.reader.epub

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.em
import com.kafka.common.extensions.rememberMutableState
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.SettingsSheet
import com.kafka.ui.components.scaffoldPadding
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.EpubBook
import kafka.reader.core.models.EpubChapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HorizontalEpubBook(
    ebook: EpubBook,
    settings: ReaderSettings,
    navigate: (String) -> Unit,
    changeSettings: (ReaderSettings) -> Unit
) {
    val chapters = ebook.chapters
    var showSettings by rememberMutableState { false }

    // Track pages for each chapter
    val pages = remember {
        mutableStateListOf<PageContent>()
    }
    val textMeasurer = rememberTextMeasurer()

    BoxWithConstraints {
        // Calculate viewport size and pages
        val density = LocalDensity.current

        LaunchedEffect(chapters, maxWidth, maxHeight) {
            val viewportWidth = with(density) { maxWidth.toPx() }
            val viewportHeight = with(density) { maxHeight.toPx() }

            // Calculate pages for all chapters
            pages.clear()
            chapters.forEachIndexed { index, chapter ->
                val chapterPages = calculatePages(
                    chapter = chapter,
                    index = index,
                    viewportWidth = viewportWidth,
                    viewportHeight = viewportHeight,
                    settings = settings,
                    textMeasurer = textMeasurer
                )
                pages.addAll(chapterPages)
            }
        }

        val pagerState = rememberPagerState(
            initialPage = 0,
            pageCount = { pages.size }
        )

        if (showSettings) {
            SettingsSheet(
                settings = settings,
                language = ebook.language,
                onDismiss = { showSettings = false },
                changeSettings = changeSettings
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .background(settings.backgroundColor)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down: PointerInputChange = awaitFirstDown()
                        val up: PointerInputChange? = waitForUpOrCancellation()
                        if (up != null && down.id == up.id) {
                            showSettings = !showSettings
                        }
                    }
                }
        ) { pageIndex ->
            PageContent(
                page = pages[pageIndex],
                settings = settings,
                navigate = navigate,
                ebook = ebook
            )
        }
    }
}

@Composable
private fun PageContent(
    page: PageContent,
    ebook: EpubBook,
    settings: ReaderSettings,
    navigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(scaffoldPadding())
    ) {
        Column {
            page.elements.forEach { element ->
                ReaderContent(element, settings, navigate, ebook)
            }
        }
    }
}

// Data class to hold page information
private data class PageContent(
    val elements: List<ContentElement>,
    val chapterIndex: Int,
    val pageIndex: Int
)

private suspend fun calculatePages(
    chapter: EpubChapter,
    index: Int,
    viewportWidth: Float,
    viewportHeight: Float,
    settings: ReaderSettings,
    textMeasurer: TextMeasurer
): List<PageContent> {
    return withContext(Dispatchers.Default) {
        val pages = mutableListOf<PageContent>()
        var currentElements = mutableListOf<ContentElement>()
        var currentHeight = 0f

        for (element in chapter.contentElements) {
            val elementHeight = when (element) {
                is ContentElement.Text -> measureTextElement(
                    element = element,
                    textMeasurer = textMeasurer,
                    settings = settings,
                    viewportWidth = viewportWidth
                )

                is ContentElement.Heading -> measureHeadingElement(
                    element = element,
                    textMeasurer = textMeasurer,
                    settings = settings,
                    viewportWidth = viewportWidth
                )
                // Add measurements for other content types as needed
                else -> 0f // Placeholder for other content types
            }

            if (currentHeight + elementHeight > viewportHeight && currentElements.isNotEmpty()) {
                // Current page is full, create new page
                pages.add(
                    PageContent(
                        elements = currentElements.toList(),
                        chapterIndex = index,
                        pageIndex = pages.size
                    )
                )
                currentElements = mutableListOf(element)
                currentHeight = elementHeight
            } else {
                currentElements.add(element)
                currentHeight += elementHeight
            }
        }

        // Add remaining elements as the last page
        if (currentElements.isNotEmpty()) {
            pages.add(
                PageContent(
                    elements = currentElements,
                    chapterIndex = index,
                    pageIndex = pages.size
                )
            )
        }

        pages
    }
}

private fun measureTextElement(
    element: ContentElement.Text,
    textMeasurer: TextMeasurer,
    settings: ReaderSettings,
    viewportWidth: Float
): Float {
    val measuredText = textMeasurer.measure(
        text = AnnotatedString(element.content),
        style = TextStyle(
            fontSize = settings.fontSize * element.sizeFactor,
            letterSpacing = element.letterSpacing?.em ?: 0.em
        ),
        constraints = Constraints(maxWidth = viewportWidth.toInt())
    )
    return measuredText.size.height.toFloat()
}

private fun measureHeadingElement(
    element: ContentElement.Heading,
    textMeasurer: TextMeasurer,
    settings: ReaderSettings,
    viewportWidth: Float
): Float {
    val headingSize = when (element.level) {
        1 -> settings.fontSize * 2.0f
        2 -> settings.fontSize * 1.5f
        3 -> settings.fontSize * 1.25f
        else -> settings.fontSize * 1.1f
    }

    val measuredText = textMeasurer.measure(
        text = AnnotatedString(element.content),
        style = TextStyle(fontSize = headingSize),
        constraints = Constraints(maxWidth = viewportWidth.toInt())
    )
    return measuredText.size.height.toFloat()
}

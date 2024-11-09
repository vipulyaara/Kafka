package com.kafka.reader.epub

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
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
import com.kafka.common.extensions.rememberMutableState
import com.kafka.reader.epub.components.CodeBlockElement
import com.kafka.reader.epub.components.HeadingElement
import com.kafka.reader.epub.components.ListElement
import com.kafka.reader.epub.components.QuoteElement
import com.kafka.reader.epub.components.TableComponent
import com.kafka.reader.epub.components.TextElement
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.settings.SettingsSheet
import com.kafka.ui.components.scaffoldPadding
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.EpubBook
import kafka.reader.core.models.EpubChapter
import ui.common.theme.theme.Dimens

@Composable
fun EpubBook(
    ebook: EpubBook,
    settings: ReaderSettings,
    lazyListState: LazyListState,
    navigate: (String) -> Unit,
    changeSettings: (ReaderSettings) -> Unit
) {
    val chapters = ebook.chapters
    var showSettings by rememberMutableState { false }

    if (showSettings) {
        SettingsSheet(
            settings = settings,
            language = ebook.language,
            onDismiss = { showSettings = false },
            changeSettings = changeSettings
        )
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier
            .fillMaxWidth()
            .background(settings.backgroundColor)
            .pointerInput(Unit) {
                awaitEachGesture {
                    val down: PointerInputChange = awaitFirstDown()
                    val up: PointerInputChange? = waitForUpOrCancellation()
                    if (up != null && down.id == up.id) {
                        showSettings = !showSettings
                    }
                }
            },
        contentPadding = scaffoldPadding()
    ) {
        items(chapters) { chapter ->
            ReaderChapter(
                ebook = ebook,
                chapter = chapter,
                settings = settings,
                navigate = navigate
            )
        }
    }
}

@Composable
internal fun ReaderChapter(
    ebook: EpubBook,
    chapter: EpubChapter,
    settings: ReaderSettings,
    navigate: (String) -> Unit
) {
    ReaderSelectionContainer {
        Column {
            chapter.contentElements.forEach { element ->
                ReaderContent(element, settings, navigate, ebook)
            }
        }
    }
}

@Composable
internal fun ColumnScope.ReaderContent(
    element: ContentElement,
    settings: ReaderSettings,
    navigate: (String) -> Unit,
    ebook: EpubBook
) {
    when (element) {
        is ContentElement.Text -> {
            TextElement(element = element, settings = settings, navigate = navigate)
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
                        .padding(vertical = Dimens.Spacing08)
                        .align(Alignment.CenterHorizontally)
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
    }
}

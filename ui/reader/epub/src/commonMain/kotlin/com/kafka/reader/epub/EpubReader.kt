package com.kafka.reader.epub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.extensions.rememberMutableState
import com.kafka.common.simpleClickable
import com.kafka.reader.epub.models.EpubChapter
import com.kafka.reader.epub.settings.ReaderSettings
import com.kafka.reader.epub.ui.SettingsSheet
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.progress.InfiniteProgressBar
import com.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun EpubReader(viewModel: EpubReaderViewModel) {
    val ebook = viewModel.ebook
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    Scaffold { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (ebook == null && loading) {
                    InfiniteProgressBar(modifier = Modifier.align(Alignment.Center))
                } else {
                    if (ebook != null) {
                        EpubBook(chapters = ebook.chapters)
                    }
                }
            }
        }
    }
}

@Composable
private fun EpubBook(chapters: List<EpubChapter>) {
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
        items(chapters) { chapter ->
            Chapter(chapter = chapter, settings = settings)
        }
    }
}

@Composable
private fun Chapter(chapter: EpubChapter, settings: ReaderSettings) {
    val paragraphs = remember { chunkText(chapter.body) }
    paragraphs.forEach { para ->
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
}

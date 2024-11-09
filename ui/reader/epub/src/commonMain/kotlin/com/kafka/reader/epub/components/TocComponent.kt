@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kafka.reader.core.models.EpubChapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ui.common.theme.theme.Dimens

@Composable
fun TocComponent(
    show: MutableState<Boolean>,
    chapters: List<EpubChapter>,
    selectChapter: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()

    if (show.value) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = {
                coroutineScope.launch {
                    sheetState.hide()
                    delay(300)
                    show.value = false
                }
            }
        ) {
            LazyColumn {
                items(chapters) { chapter ->
                    ChapterHeading(
                        text = chapter.title,
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                sheetState.hide()
                                delay(300)
                                show.value = false
                            }
                            selectChapter(chapter.chapterId)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChapterHeading(text: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize(), color = Color.Transparent) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(
                    horizontal = Dimens.Gutter,
                    vertical = Dimens.Spacing16
                )
        )
    }
}

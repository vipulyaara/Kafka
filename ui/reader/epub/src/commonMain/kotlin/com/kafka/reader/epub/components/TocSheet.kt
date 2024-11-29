@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.kafka.ui.components.material.ModalBottomSheet
import com.kafka.ui.components.search.SearchWidget
import kafka.reader.core.models.NavPoint
import kafka.ui.reader.epub.generated.resources.Res
import kafka.ui.reader.epub.generated.resources.chapters
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun TocSheet(tocState: TocState, navPoints: List<NavPoint>, onNavPointClicked: (String) -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }


    // TODO - double check this
    val filteredChapters = navPoints.filter { chapter ->
        chapter.title.contains(searchQuery, ignoreCase = true)
    }

    val dismissSheet: () -> Unit = {
        coroutineScope
            .launch { sheetState.hide() }
            .invokeOnCompletion { tocState.hide() }
    }

    ModalBottomSheet(
        show = tocState.show,
        sheetState = sheetState,
        onDismissRequest = tocState::hide
    ) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item { Label(filteredChapters.size) }

            item {
                SearchWidget(
                    searchText = searchQuery,
                    setSearchText = { searchQuery = it },
                    onImeAction = { /* empty as search is real-time */ }
                )
            }

            item { Spacer(Modifier.height(Dimens.Spacing12)) }

            items(filteredChapters) { navPoint ->
                NavPointHeading(
                    navPoint = navPoint,
                    level = 0,
                    onNavPointClicked = {
                        dismissSheet()
                        onNavPointClicked(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun Label(chapterSize: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.padding(horizontal = Dimens.Gutter, vertical = Dimens.Spacing16),
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = stringResource(Res.string.chapters),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = chapterSize.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(start = Dimens.Spacing04, bottom = Dimens.Spacing02)
        )
    }
}

@Composable
private fun NavPointHeading(
    modifier: Modifier = Modifier,
    navPoint: NavPoint,
    level: Int,
    onNavPointClicked: (String) -> Unit = {}
) {
    val indentation = with(LocalDensity.current) { Dimens.Spacing12.toPx() }

    Surface(
        modifier = modifier
            .fillMaxSize()
            .clickable { onNavPointClicked(navPoint.src) },
        color = Color.Transparent
    ) {
        Text(
            text = navPoint.title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .padding(
                    start = Dimens.Gutter + (level * indentation).dp,
                    end = Dimens.Gutter,
                    top = Dimens.Spacing16,
                    bottom = Dimens.Spacing16
                )
        )
    }

    navPoint.children.forEach { child ->
        NavPointHeading(
            navPoint = child,
            level = level + 1,
            onNavPointClicked = onNavPointClicked
        )
    }
}

class TocState {
    var show by mutableStateOf(false)

    fun show() {
        show = true
    }

    fun hide() {
        show = false
    }
}

@Composable
fun rememberTocState() = remember { TocState() }

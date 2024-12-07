@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.image.Icons
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
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }

    val filteredNavPoints = navPoints.filter { navPoint ->
        navPoint.title.contains(searchQuery, ignoreCase = true)
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
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize()
        ) {
            item { Label(filteredNavPoints.size) }

            item {
                SearchWidget(
                    searchText = searchQuery,
                    setSearchText = { searchQuery = it },
                    onImeAction = { /* empty as search is real-time */ }
                )
            }

            item { Spacer(Modifier.height(Dimens.Spacing12)) }

            items(filteredNavPoints) { navPoint ->
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
    navPoint: NavPoint,
    level: Int,
    modifier: Modifier = Modifier,
    onNavPointClicked: (String) -> Unit = {}
) {
    val indentation = with(LocalDensity.current) { Dimens.Spacing12.toPx() }
    var isExpanded by remember { mutableStateOf(false) }
    val hasChildren = navPoint.children.isNotEmpty()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavPointClicked(navPoint.src) }
                .padding(vertical = if (hasChildren) Dimens.Spacing04 else Dimens.Spacing16)
                .padding(
                    start = Dimens.Gutter + (level * indentation).dp,
                    end = Dimens.Gutter
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (hasChildren) {
                IconButton(
                    onClick = { isExpanded = !isExpanded },
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.ChevronRight else Icons.ChevronDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                // Add spacing equivalent to the icon button for consistent indentation
                Spacer(modifier = Modifier.padding(start = 40.dp))
            }

            Text(
                text = navPoint.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }

    AnimatedVisibilityFade(isExpanded) {
        Column {
            navPoint.children.forEach { child ->
                NavPointHeading(
                    navPoint = child,
                    level = level + 1,
                    onNavPointClicked = onNavPointClicked
                )
            }
        }
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

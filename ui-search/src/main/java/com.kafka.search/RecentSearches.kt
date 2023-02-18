package com.kafka.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconResource
import ui.common.theme.theme.Dimens

@Composable
fun RecentSearches(
    recentSearches: List<String>,
    onSearchClicked: (String) -> Unit,
    onRemoveSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 1.dp,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .padding(vertical = Dimens.Spacing20)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
            .animateContentSize()
    ) {
        LazyColumn(modifier = modifier.padding(vertical = 24.dp)) {

            item {
                recentSearches.takeIf { it.isNotEmpty() }?.let {
                    SearchResultLabel("Recent searches")
                }
            }

            items(recentSearches) {
                RecentSearchItem(
                    searchTerm = it,
                    modifier = Modifier.animateItemPlacement(),
                    onSearchClicked = onSearchClicked,
                    onRemoveSearch = onRemoveSearch
                )
            }
        }
    }
}

@Composable
private fun RecentSearchItem(
    searchTerm: String,
    modifier: Modifier = Modifier,
    onSearchClicked: (String) -> Unit,
    onRemoveSearch: (String) -> Unit
) {
    Row(
        modifier = modifier
            .clickable(onClick = { onSearchClicked(searchTerm) })
            .padding(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing08),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = searchTerm,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )

        IconResource(
            modifier = Modifier
                .clickable(onClick = { onRemoveSearch(searchTerm) })
                .padding(10.dp)
                .size(24.dp),
            imageVector = Icons.XCircle
        )
    }
}

@Composable
private fun SearchResultLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        modifier = modifier.padding(
            start = Dimens.Spacing12, end = 24.dp, bottom = Dimens.Spacing12
        ),
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.secondary
    )
}

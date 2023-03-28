package com.kafka.search

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.kafka.common.animation.Delayed
import org.kafka.common.image.Icons
import org.kafka.common.widgets.IconResource
import org.kafka.search.R
import ui.common.theme.theme.Dimens

@Composable
fun RecentSearches(
    recentSearches: List<String>,
    onSearchClicked: (String) -> Unit,
    onRemoveSearch: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Delayed {
        Surface(color = MaterialTheme.colorScheme.background) {
            Column(modifier = modifier.animateContentSize()) {
                recentSearches.takeIf { it.isNotEmpty() }?.let {
                    SearchResultLabel(stringResource(R.string.recent_searches))
                }

                recentSearches.forEach {
                    RecentSearchItem(
                        searchTerm = it,
                        onSearchClicked = onSearchClicked,
                        onRemoveSearch = onRemoveSearch
                    )
                }
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
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface
        )

        IconResource(
            modifier = Modifier
                .clickable(onClick = { onRemoveSearch(searchTerm) })
                .padding(10.dp)
                .size(Dimens.Spacing24),
            imageVector = Icons.XCircle
        )
    }
}

@Composable
private fun SearchResultLabel(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        modifier = modifier.padding(Dimens.Spacing16),
        color = MaterialTheme.colorScheme.secondary
    )
}

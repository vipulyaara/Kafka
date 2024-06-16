package com.kafka.search

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kafka.data.model.MediaType
import com.kafka.data.model.SearchFilter
import org.kafka.common.image.Icons
import org.kafka.common.simpleClickable
import ui.common.theme.theme.Dimens

@Composable
internal fun SearchFilterChips(
    selectedFilters: List<SearchFilter>,
    onFilterClicked: (SearchFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(modifier = modifier) {
        items(SearchFilter.entries) { filter ->
            val selected = selectedFilters.contains(filter)
            FilterChip(
                title = filter.name,
                selected = selected,
                modifier = Modifier
                    .padding(horizontal = Dimens.Spacing02)
                    .animateContentSize(),
                onClick = { onFilterClicked(filter) },
            )
        }
    }
}

@Composable
internal fun MediaTypeChips(
    selectedMediaTypes: List<MediaType>,
    onFilterClicked: (MediaType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        MediaType.entries.forEach { filter ->
            val selected = selectedMediaTypes.contains(filter)
            FilterChip(
                title = filter.name,
                selected = selected,
                modifier = Modifier
                    .padding(horizontal = Dimens.Spacing02)
                    .animateContentSize(),
                onClick = { onFilterClicked(filter) },
            )
        }
    }
}

@Composable
private fun FilterChip(
    modifier: Modifier,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val boarderColor by animateColorAsState(
        if (selected) colorScheme.secondaryContainer else colorScheme.surfaceVariant, label = ""
    )
    val containerColor by animateColorAsState(
        if (selected) colorScheme.secondaryContainer else colorScheme.background, label = ""
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.RadiusMedium),
        color = containerColor,
        border = BorderStroke(Dimens.Spacing01, boarderColor),
    ) {
        Row(
            modifier = Modifier
                .simpleClickable { onClick() }
                .padding(vertical = Dimens.Spacing06, horizontal = Dimens.Spacing12),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing04)
        ) {
            if (selected) {
                Icon(
                    modifier = Modifier.size(Dimens.Spacing16),
                    imageVector = Icons.Check,
                    contentDescription = null
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = contentColorFor(containerColor)
            )
        }
    }
}

package org.kafka.homepage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.common.ImmutableList
import org.kafka.common.widgets.LoadImage
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary
import ui.common.theme.theme.textSecondary

@Composable
fun FeaturedItems(
    readingList: ImmutableList<Item>,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    if (readingList.items.isNotEmpty()) {
        Column(
            modifier = modifier
                .padding(vertical = Dimens.Spacing12)
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(vertical = Dimens.Spacing24)
        ) {
            Text(
                text = "Featured Items",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.textSecondary,
                modifier = Modifier.padding(horizontal = Dimens.Spacing20)
            )
            LazyRow(
                modifier = Modifier.padding(top = Dimens.Spacing12),
                contentPadding = PaddingValues(horizontal = Dimens.Spacing12)
            ) {
                val items = readingList.items.subList(0, readingList.size.coerceAtMost(10))
                items(items = items, key = { it.itemId }) {
                    FeaturedItem(item = it, openItemDetail = openItemDetail)
                }
            }
        }
    }
}

@Composable
private fun FeaturedItem(item: Item, openItemDetail: (String) -> Unit) {
    Column(
        modifier = Modifier
            .width(164.dp)
            .padding(Dimens.Spacing04)
            .clickable { openItemDetail(item.itemId) }
    ) {
        LoadImage(
            data = item.coverImage,
            modifier = Modifier
                .size(164.dp)
                .aspectRatio(1f)
                .padding(bottom = Dimens.Spacing04),
        )

        Text(
            text = item.title.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.textPrimary,
            maxLines = 1,
        )

        Text(
            text = item.creator?.name.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.secondary,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = Dimens.Spacing02),
            maxLines = 1,
        )
    }
}

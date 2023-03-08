package org.kafka.homepage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.kafka.data.entities.FavoriteItem
import org.kafka.common.ImmutableList
import org.kafka.common.widgets.LoadImage
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary

@Composable
fun FeaturedItems(
    items: ImmutableList<FavoriteItem>,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    if (items.items.isNotEmpty()) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = Dimens.Spacing12)
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f))
                .padding(vertical = Dimens.Spacing24)
        ) {
            Text(
                text = "Favorites",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(horizontal = Dimens.Spacing20)
            )
            LazyRow(
                modifier = Modifier.padding(top = Dimens.Spacing08),
                contentPadding = PaddingValues(horizontal = Dimens.Spacing12)
            ) {
                items(
                    items = items.items.subList(0, items.size.coerceAtMost(10)),
                    key = { it.itemId }
                ) {
                    FeaturedItem(item = it, openItemDetail = openItemDetail)
                }
            }
        }
    }
}

@Composable
private fun FeaturedItem(item: FavoriteItem, openItemDetail: (String) -> Unit) {
    Column(
        modifier = Modifier
            .width(148.dp)
            .padding(Dimens.Spacing08)
            .clickable { openItemDetail(item.itemId) }
    ) {
        LoadImage(
            data = item.coverImage,
            modifier = Modifier
                .size(148.dp)
                .aspectRatio(1f)
                .padding(bottom = Dimens.Spacing04),
        )

        Text(
            text = item.title,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.textPrimary,
            maxLines = 1,
        )

        Text(
            text = item.creator,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.secondary,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = Dimens.Spacing02),
            maxLines = 1,
        )
    }
}

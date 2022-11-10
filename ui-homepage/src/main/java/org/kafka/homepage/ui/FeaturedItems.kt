package org.kafka.homepage.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.common.ImmutableList
import org.kafka.common.widgets.LoadImage
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary

@Composable
fun FeaturedItems(
    readingList: ImmutableList<Item>,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = Dimens.Spacing12)
    ) {
        items(readingList.items, key = { it.itemId }) {
            FeaturedItem(item = it, openItemDetail = openItemDetail)
        }
    }
}

@Composable
private fun FeaturedItem(item: Item, openItemDetail: (String) -> Unit) {
    Column(
        modifier = Modifier
            .width(164.dp)
            .clickable { openItemDetail(item.itemId) }
    ) {
        LoadImage(
            data = item.coverImage,
            modifier = Modifier
                .size(164.dp)
                .aspectRatio(1f)
                .padding(Dimens.Spacing04),
        )

        Text(
            text = item.title.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.textPrimary,
            maxLines = 1,
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = item.mediaType.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            maxLines = 1,
        )
    }
}

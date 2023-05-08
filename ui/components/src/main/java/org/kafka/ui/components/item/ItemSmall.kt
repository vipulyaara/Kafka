package org.kafka.ui.components.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.kafka.data.entities.Item
import ui.common.theme.theme.Dimens

@Composable
fun ItemSmall(
    item: Item,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    ItemSmall(
        title = item.title,
        creator = item.creator?.name,
        mediaType = item.mediaType,
        coverImage = item.coverImage,
        itemId = item.itemId,
        modifier = modifier,
        openItemDetail = openItemDetail
    )
}

@Composable
fun ItemSmall(
    title: String?,
    creator: String?,
    mediaType: String?,
    coverImage: String?,
    itemId: String,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { openItemDetail(itemId) },
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        CoverImage(
            data = coverImage,
            modifier = Modifier.align(Alignment.CenterVertically),
            size = Dimens.CoverSizeSmall
        )
        ItemDescription(
            title = { ItemTitleSmall(title) },
            creator = { ItemCreatorSmall(creator) },
            mediaType = { ItemMediaType(mediaType) },
            modifier = Modifier.padding(vertical = Dimens.Spacing02)
        )
    }
}

@Composable
fun ItemTitleSmall(title: String?, maxLines: Int = 1) {
    Text(
        text = title.orEmpty(),
        style = MaterialTheme.typography.titleSmall,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ItemCreatorSmall(creator: String?, modifier: Modifier = Modifier) {
    Text(
        text = creator.orEmpty(),
        style = MaterialTheme.typography.labelSmall,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    )
}

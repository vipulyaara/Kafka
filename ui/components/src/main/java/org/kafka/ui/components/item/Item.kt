package org.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.entities.Item
import org.kafka.common.widgets.shadowMaterial
import org.kafka.ui.components.R
import ui.common.theme.theme.Dimens

@Composable
fun Item(
    item: Item,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    Item(
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
fun Item(
    item: FavoriteItem,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    Item(
        title = item.title,
        creator = item.creator,
        mediaType = item.mediaType,
        coverImage = item.coverImage,
        itemId = item.itemId,
        modifier = modifier,
        openItemDetail = openItemDetail
    )
}

@Composable
fun Item(
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
            .clickable { openItemDetail(itemId) }
            .padding(vertical = Dimens.Spacing08, horizontal = Dimens.Spacing16),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing16)
    ) {
        CoverImage(coverImage)
        ItemDescription(title, creator, mediaType)
    }
}

@Composable
fun CoverImage(coverImage: String?) {
    Box(
        modifier = Modifier.shadowMaterial(
            elevation = Dimens.Spacing08,
            shape = RoundedCornerShape(Dimens.RadiusSmall)
        )
    ) {
        AsyncImage(
            model = coverImage,
            placeholder = painterResource(id = R.drawable.ic_absurd_bulb),
            contentDescription = "Cover",
            modifier = Modifier
                .size(72.dp, 84.dp)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ItemDescription(title: String?, creator: String?, mediaType: String?) {
    Column {
        ItemTitle(title)
        Spacer(modifier = Modifier.height(Dimens.Spacing02))
        ItemCreator(creator)
        Spacer(modifier = Modifier.height(Dimens.Spacing04))
        ItemType(mediaType)
    }
}

@Composable
fun ItemTitle(title: String?) {
    Text(
        text = title.orEmpty(),
        style = MaterialTheme.typography.titleSmall,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ItemCreator(creator: String?) {
    Text(
        text = creator.orEmpty(),
        style = MaterialTheme.typography.labelMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.secondary
    )
}

@Composable
fun ItemType(mediaType: String?) {
    Text(
        text = mediaType.orEmpty(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.tertiary
    )
}

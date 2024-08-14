package org.kafka.ui.components.item

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import com.kafka.data.entities.Item
import org.kafka.ui.components.placeholder.placeholderDefault
import ui.common.theme.theme.Dimens

@Composable
fun RowItem(item: Item, modifier: Modifier = Modifier) {
    RowItem(
        coverImage = item.coverImage,
        title = item.title,
        mediaType = item.mediaType,
        creator = item.creator?.name,
        placeholder = placeholder(item.mediaType),
        modifier = modifier
    )
}

@Composable
fun RowItem(
    coverImage: Any?,
    modifier: Modifier = Modifier,
    title: String? = null,
    mediaType: String? = null,
    creator: String? = null,
    placeholder: ImageVector = CoverDefaults.placeholder,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CoverImage(
            data = coverImage,
            size = Dimens.CoverSizeLarge,
            placeholder = placeholder,
            shape = RoundedCornerShape(Dimens.RadiusMedium)
        )

        Column(
            modifier = Modifier.padding(
                vertical = Dimens.Spacing08,
                horizontal = Dimens.Spacing04
            )
        ) {
            ItemTitleSmall(title = title.orEmpty())
            Spacer(modifier = Modifier.height(Dimens.Spacing02))
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing08),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (mediaType != null) {
                    ItemMediaType(mediaType = mediaType, size = Dimens.IconSizeSmall)
                }
                ItemCreatorSmall(creator = creator)
            }
        }
    }
}

@Composable
fun RowItemPlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(Dimens.CoverSizeLarge)
            .clip(RoundedCornerShape(Dimens.RadiusMedium))
            .placeholderDefault()
    )
}
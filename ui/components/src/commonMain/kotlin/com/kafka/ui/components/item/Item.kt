package com.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.kafka.common.extensions.alignCenter
import com.kafka.common.image.Icons
import com.kafka.common.testTagUi
import com.kafka.common.widgets.IconResource
import com.kafka.data.entities.Item
import com.kafka.data.model.MediaType
import com.kafka.ui.components.placeholder.placeholderDefault
import kafka.ui.components.generated.resources.Res
import kafka.ui.components.generated.resources.explicit
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun Item(item: Item, modifier: Modifier = Modifier) {
    Item(
        title = item.title,
        creator = item.creator,
        mediaType = item.mediaType,
        coverImage = item.coverImage,
        modifier = modifier.testTagUi("content_item"),
        isInAppropriate = item.isInappropriate
    )
}

@Composable
fun Item(
    title: String?,
    creator: String?,
    mediaType: MediaType,
    coverImage: String?,
    modifier: Modifier = Modifier,
    isInAppropriate: Boolean = false,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing16)
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.CoverSizeMedium)
                .align(Alignment.CenterVertically)
        ) {
            CoverImage(
                data = coverImage,
                placeholder = placeholder(mediaType),
                size = Dimens.CoverSizeMedium
            )

            if (isInAppropriate) {
                Text(
                    text = stringResource(Res.string.explicit),
                    style = MaterialTheme.typography.labelMedium.alignCenter(),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(Dimens.Spacing04))
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .padding(vertical = Dimens.Spacing04)
                )
            }
        }

        ItemDescription(title = { ItemTitleMedium(title) },
            creator = { ItemCreatorSmall(creator) },
            mediaType = { ItemMediaType(mediaType) })
    }
}

@Composable
fun ItemDescription(
    title: @Composable () -> Unit,
    creator: @Composable () -> Unit,
    mediaType: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.padding(vertical = Dimens.Spacing04)) {
        title()
        Spacer(modifier = Modifier.height(Dimens.Spacing04))
        Row(verticalAlignment = Alignment.CenterVertically) {
            creator()
        }
        Spacer(modifier = Modifier.height(Dimens.Spacing12))
        mediaType()
    }
}

@Composable
fun ItemTitleMedium(title: String?, maxLines: Int = 2) {
    Text(
        text = title.orEmpty(),
        style = MaterialTheme.typography.titleMedium,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
fun ItemCreator(creator: String?, modifier: Modifier = Modifier) {
    Text(
        text = creator.orEmpty(),
        style = MaterialTheme.typography.labelMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.secondary,
        modifier = modifier
    )
}

@Composable
fun ItemMediaType(mediaType: MediaType, modifier: Modifier = Modifier, size: Dp = 16.dp) {
    IconResource(
        imageVector = placeholder(mediaType),
        tint = MaterialTheme.colorScheme.secondary,
        modifier = modifier
            .size(size)
            .testTagUi("item_${mediaType}_icon"),
        contentDescription = mediaType.value
    )
}

@Composable
fun ItemSmall(item: Item, modifier: Modifier = Modifier) {
    ItemSmall(
        title = item.title,
        creator = item.creator,
        mediaType = item.mediaType,
        coverImage = item.coverImage,
        modifier = modifier
    )
}

@Composable
fun ItemSmall(
    title: String?,
    creator: String?,
    mediaType: MediaType,
    coverImage: String?,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12)
    ) {
        CoverImage(
            data = coverImage,
            placeholder = placeholder(mediaType),
            modifier = Modifier.align(Alignment.CenterVertically),
            size = Dimens.CoverSizeSmall
        )
        ItemDescription(
            title = { ItemTitleSmall(title, maxLines = 2) },
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

fun placeholder(mediaType: MediaType) = if (mediaType.isAudio) Icons.Audio else Icons.Texts

@Composable
fun ItemPlaceholder(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Dimens.Gutter)
    ) {
        Box(
            modifier = Modifier
                .size(Dimens.CoverSizeSmall)
                .clip(RoundedCornerShape(Dimens.Spacing04))
                .placeholderDefault()
        )

        Column(Modifier.padding(vertical = Dimens.Spacing04)) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(16.dp)
                    .clip(RoundedCornerShape(Dimens.Spacing04))
                    .placeholderDefault()
            )

            Spacer(modifier = Modifier.height(Dimens.Spacing08))

            Box(
                modifier = Modifier
                    .width(96.dp)
                    .height(20.dp)
                    .clip(RoundedCornerShape(Dimens.Spacing04))
                    .placeholderDefault()
            )
        }
    }
}

package org.kafka.ui.components.item

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionScope
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
import com.kafka.data.entities.Item
import org.kafka.ui.components.placeholder.placeholderDefault
import ui.common.theme.theme.Dimens

@Composable
fun SharedTransitionScope.RowItem(
    item: Item,
    animatedContentScope: AnimatedContentScope,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        CoverImage(
            data = item.coverImage,
            size = Dimens.CoverSizeLarge,
            placeholder = placeholder(item.mediaType),
            shape = RoundedCornerShape(Dimens.RadiusMedium),
            imageModifier = Modifier.sharedElement(
                state = rememberSharedContentState(key = item.coverImage.orEmpty()),
                animatedVisibilityScope = animatedContentScope
            )
        )

        Column(
            modifier = Modifier.padding(
                vertical = Dimens.Spacing08,
                horizontal = Dimens.Spacing04
            )
        ) {
            ItemTitleSmall(
                title = item.title.orEmpty(),
                modifier = Modifier.sharedElement(
                    state = rememberSharedContentState(key = item.title.orEmpty()),
                    animatedVisibilityScope = animatedContentScope
                )
            )
            Spacer(modifier = Modifier.height(Dimens.Spacing02))
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing08),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemMediaType(mediaType = item.mediaType, size = Dimens.IconSizeSmall)
                ItemCreatorSmall(creator = item.creator?.name)
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
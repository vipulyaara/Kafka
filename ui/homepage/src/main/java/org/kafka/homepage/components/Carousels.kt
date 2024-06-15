package org.kafka.homepage.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import kotlinx.collections.immutable.ImmutableList
import org.kafka.ui.components.item.FeaturedItem
import ui.common.theme.theme.Dimens

@Composable
internal fun Carousels(
    carouselItems: ImmutableList<Item>,
    images: List<String>,
    onBannerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    val state = rememberCarouselState { carouselItems.size }

    HorizontalMultiBrowseCarousel(
        state = state,
        preferredItemWidth = 350.dp,
        minSmallItemWidth = 56.dp,
        maxSmallItemWidth = 72.dp,
        modifier = modifier.padding(Dimens.Spacing02),
        contentPadding = PaddingValues(horizontal = Dimens.Spacing12),
        itemSpacing = Dimens.Spacing08
    ) { index ->
        carouselItems.getOrNull(index)?.let { item ->
            FeaturedItem(
                item = item,
                label = item.title,
                imageUrl = images.getOrNull(index),
                onClick = { onBannerClick(item.itemId) },
                modifier = Modifier.maskClip(shape = RoundedCornerShape(Dimens.Radius20))
            )
        }
    }
}

@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.homepage.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui.components.item.FeaturedItem
import ui.common.theme.theme.Dimens

@Composable
internal fun Carousels(
    carouselItems: List<Item>,
    images: List<String>,
    onBannerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberCarouselState { carouselItems.size }

    HorizontalMultiBrowseCarousel(
        state = state,
        modifier = modifier.padding(Dimens.Spacing08),
        preferredItemWidth = CarouselItemPreferredWidth.dp,
        itemSpacing = Dimens.Spacing04,
        contentPadding = PaddingValues(horizontal = Dimens.Spacing16)
    ) { index ->
        carouselItems.getOrNull(index)?.let { item ->
            FeaturedItem(
                item = item,
                label = null,
                imageUrl = images.getOrNull(index),
                onClick = { onBannerClick(item.itemId) },
                modifier = Modifier.maskClip(shape = RoundedCornerShape(Dimens.Radius16))
            )
        }
    }
}

const val CarouselItemPreferredWidth = 400

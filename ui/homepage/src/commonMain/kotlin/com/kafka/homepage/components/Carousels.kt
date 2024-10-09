@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.kafka.homepage.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
) {
    val state = rememberPagerState { carouselItems.size }

    Column(
        modifier = modifier.padding(top = Dimens.Spacing12),
        horizontalAlignment = Alignment.End
    ) {
        HorizontalPager(
            state = state,
            modifier = Modifier.padding(Dimens.Spacing02),
            contentPadding = PaddingValues(horizontal = Dimens.Spacing24),
            pageSpacing = Dimens.Spacing04,
        ) { index ->
            carouselItems.getOrNull(index)?.let { item ->
                FeaturedItem(
                    item = item,
                    label = item.title.takeIf { showLabel },
                    imageUrl = images.getOrNull(index),
                    onClick = { onBannerClick(item.itemId) }
                )
            }
        }
    }
}

@Composable
internal fun CarouselsMaterial3(
    carouselItems: List<Item>,
    images: List<String>,
    onBannerClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    showLabel: Boolean = true,
) {
    val state = rememberCarouselState { carouselItems.size }
    HorizontalMultiBrowseCarousel(
        state = state,
        modifier = modifier.padding(horizontal = Dimens.Spacing08, vertical = Dimens.Spacing08),
        preferredItemWidth = CarouselItemPreferredWidth.dp,
        itemSpacing = Dimens.Spacing04,
        contentPadding = PaddingValues(horizontal = Dimens.Spacing16)
    ) { index ->
        carouselItems.getOrNull(index)?.let { item ->
            FeaturedItem(
                item = item,
                label = item.title.takeIf { showLabel },
                imageUrl = images.getOrNull(index),
                onClick = { onBannerClick(item.itemId) },
                modifier = Modifier.maskClip(shape = RoundedCornerShape(Dimens.Radius16))
            )
        }
    }
}

const val CarouselItemPreferredWidth = 400

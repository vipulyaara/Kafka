@file:OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)

package com.kafka.homepage.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kafka.common.adaptive.isExpanded
import com.kafka.common.adaptive.windowWidthSizeClass
import com.kafka.common.animation.LocalAnimatedContentScope
import com.kafka.common.animation.LocalSharedTransitionScope
import com.kafka.common.extensions.black
import com.kafka.data.entities.Item
import com.kafka.navigation.graph.Screen.ItemDetail.Origin
import com.kafka.navigation.graph.Screen.ItemDetail.SharedElementCoverKey
import com.kafka.ui.components.material.HorizontalCubePager
import com.sarahang.playback.ui.color.DynamicTheme
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.isDark

@Composable
fun FeaturedItemsPager(
    carouselItems: List<Item>,
    images: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalTheme.current.isDark()

    PagerScaffold(items = carouselItems, modifier = modifier) { page ->
        val item = carouselItems[page]
        val image = images.getOrNull(page) ?: item.coverImage

        DynamicTheme(model = image, useDarkTheme = isDark) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onClick(carouselItems[page].itemId) }
            ) {
                CoverImage(image = image, title = item.title)
                Description(item = item, date = dates.getOrNull(page))
            }
        }
    }
}

@Composable
private fun PagerScaffold(
    items: List<Item>,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit
) {
    val isExpanded = windowWidthSizeClass().isExpanded()

    if (isExpanded) {
        val state = rememberCarouselState { items.size }
        HorizontalMultiBrowseCarousel(
            state = state,
            modifier = modifier.padding(Dimens.Spacing08),
            preferredItemWidth = CarouselItemPreferredWidth.dp,
            itemSpacing = Dimens.Spacing04,
            contentPadding = PaddingValues(horizontal = Dimens.Spacing16)
        ) { index ->
            Box(Modifier.maskClip(shape = RoundedCornerShape(Dimens.Radius16))) {
                content(index)
            }
        }
    } else {
        val state = rememberPagerState { items.size }
        HorizontalCubePager(state = state, modifier = modifier) { page ->
            content(page)
        }
    }
}

@Composable
private fun CoverImage(image: String?, title: String? = null) {
    with(LocalSharedTransitionScope.current) {
        AsyncImage(
            model = image,
            contentDescription = title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(0.66f)
                .sharedElement(
                    animatedVisibilityScope = LocalAnimatedContentScope.current,
                    state = rememberSharedContentState(
                        key = SharedElementCoverKey(cover = image, origin = Origin.Carousel)
                    )
                )
        )
    }
}

@Composable
private fun Description(item: Item, date: String?) {
    Row(
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer),
        verticalAlignment = Alignment.CenterVertically
    ) {
        date?.let {
            Text(
                text = date.uppercase(),
                modifier = Modifier
                    .vertical()
                    .rotate(-90f),
                style = MaterialTheme.typography.titleLarge.black(),
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
            )
        }

        Text(
            text = item.description.orEmpty(),
            modifier = Modifier
                .padding(vertical = Dimens.Spacing20)
                .padding(end = Dimens.Spacing20)
                .padding(start = Dimens.Spacing08),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            minLines = 3,
            maxLines = 3
        )
    }
}

fun Modifier.vertical() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2)
        )
    }
}

private val dates = listOf(
    "21 Nov",
    "22 Nov",
    "23 Nov",
    "24 Nov",
    "25 Nov",
    "26 Nov",
    "27 Nov",
).reversed()

@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.kafka.homepage.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.CarouselItemScope
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kafka.common.animation.LocalAnimatedContentScope
import com.kafka.common.animation.LocalSharedTransitionScope
import com.kafka.common.image.Icons
import com.kafka.data.entities.Item
import com.kafka.navigation.graph.Screen.ItemDetail.Origin
import com.kafka.navigation.graph.Screen.ItemDetail.SharedElementCoverKey
import com.kafka.ui.components.item.FeaturedItem
import com.materialkolor.PaletteStyle
import com.sarahang.playback.ui.color.DynamicTheme
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.isDark

@Composable
internal fun FullPageCarousels(
    carouselItems: List<Item>,
    images: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberCarouselState { carouselItems.size }
    val isDark = LocalTheme.current.isDark()

    Column(modifier = modifier) {
//        Header()

        Spacer(Modifier.height(Dimens.Spacing08))

        HorizontalMultiBrowseCarousel(
            state = state,
            modifier = Modifier.padding(Dimens.Spacing08),
            preferredItemWidth = CarouselItemPreferredWidth.dp,
            itemSpacing = Dimens.Spacing04,
            contentPadding = PaddingValues(horizontal = Dimens.Spacing16)
        ) { index ->
            val image = images.getOrNull(index) ?: carouselItems.getOrNull(index)?.coverImage

            DynamicTheme(model = image, useDarkTheme = isDark, style = PaletteStyle.Neutral) {
                Box(Modifier.height(IntrinsicSize.Max)) {
                    AsyncImage(
                        model = image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.Radius16))
                            .maskClip(shape = RoundedCornerShape(Dimens.Radius16))
                            .blur(48.dp)
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(Dimens.Radius16))
                            .maskClip(shape = RoundedCornerShape(Dimens.Radius16))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f))
                            .fillMaxWidth()
                            .fillMaxHeight()
                    )

                    carouselItems.getOrNull(index)?.let { item ->
                        CarouselItem(item = item, index = index, image = image, onClick = onClick)
                    }
                }
            }
        }
    }
}

@Composable
private fun CarouselItemScope.CarouselItem(
    item: Item,
    index: Int,
    image: String?,
    onClick: (String) -> Unit
) {
    Column {
        Spacer(Modifier.height(Dimens.Spacing12))

        Text(
            text = dates.getOrNull(index).orEmpty(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .padding(horizontal = Dimens.Gutter)
                .padding(end = Dimens.Spacing08)
                .align(Alignment.End)
        )

        Spacer(Modifier.height(Dimens.Spacing04))

        with(LocalSharedTransitionScope.current) {
            FeaturedItem(
                item = item,
                label = null,
                aspectRatio = 0.66f,
                imageUrl = image,
                onClick = { onClick(item.itemId) },
                modifier = Modifier
                    .padding(Dimens.Spacing08)
                    .sharedElement(
                        state = rememberSharedContentState(
                            key = SharedElementCoverKey(
                                cover = item.coverImage.orEmpty(),
                                origin = Origin.Carousel
                            )
                        ),
                        animatedVisibilityScope = LocalAnimatedContentScope.current
                    )
                    .maskClip(shape = RoundedCornerShape(Dimens.Radius16)),
            )
        }

        Spacer(Modifier.height(Dimens.Spacing04))

        Box(modifier = Modifier.padding(horizontal = Dimens.Spacing24)) {
            Text(
                text = "\n",
                style = MaterialTheme.typography.titleSmall,
                color = Color.Transparent,
                minLines = 2,
                maxLines = 2
            )

            Text(
                text = item.description.orEmpty(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(Modifier.height(Dimens.Spacing12))
    }
}

@Composable
internal fun Header() {
    Column {
        Text(
            text = "Books of",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
            modifier = Modifier.padding(horizontal = Dimens.Spacing36)
        )

        Row {
            Text(
                text = "Week 2",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(start = Dimens.Spacing36, end = Dimens.Spacing12)
                    .align(Alignment.CenterVertically)
            )

            Icon(
                imageVector = Icons.ArrowForwardCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(bottom = Dimens.Spacing04)
                    .size(Dimens.Spacing36)
                    .align(Alignment.Bottom)
            )
        }
    }
}

val dates = listOf(
    "21 November 2024",
    "22 November 2024",
    "23 November 2024",
    "24 November 2024",
    "25 November 2024",
    "26 November 2024",
)

@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)

package com.kafka.homepage.components

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kafka.common.animation.LocalAnimatedContentScope
import com.kafka.common.animation.LocalSharedTransitionScope
import com.kafka.common.animation.coverImageKey
import com.kafka.common.image.Icons
import com.kafka.data.entities.Item
import com.kafka.ui.components.item.FeaturedItem
import ui.common.theme.theme.Dimens

@Composable
internal fun FullPageCarousels(
    carouselItems: List<Item>,
    images: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberCarouselState { carouselItems.size }

    Column {
        Header()

        HorizontalMultiBrowseCarousel(
            state = state,
            modifier = modifier.padding(Dimens.Spacing08),
            preferredItemWidth = CarouselItemPreferredWidth.dp,
            itemSpacing = Dimens.Spacing04,
            contentPadding = PaddingValues(horizontal = Dimens.Spacing16)
        ) { index ->
            carouselItems.getOrNull(index)?.let { item ->
                Column {
                    Text(
                        text = dates.getOrNull(index).orEmpty(),
                        style = MaterialTheme.typography.labelSmall,
                        color = colors[index],
                        modifier = Modifier
                            .padding(horizontal = Dimens.Gutter)
                            .padding(end = Dimens.Spacing08)
                            .align(Alignment.End)
                    )

                    Spacer(Modifier.height(Dimens.Spacing08))

                    with(LocalSharedTransitionScope.current) {
                        FeaturedItem(
                            item = item,
                            label = null,
                            aspectRatio = 0.66f,
                            imageUrl = images.getOrNull(index),
                            onClick = { onClick(item.itemId) },
                            modifier = Modifier
                                .maskClip(shape = RoundedCornerShape(Dimens.Radius16))
                                .sharedElement(
                                    state = rememberSharedContentState(key = coverImageKey(item.coverImage)),
                                    animatedVisibilityScope = LocalAnimatedContentScope.current
                                ),
                        )
                    }

                    if (item.description != null) {
                        Spacer(Modifier.height(Dimens.Spacing12))

                        Text(
                            text = item.description.orEmpty(),
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = Dimens.Gutter)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Header() {
    Column {
        Text(
            text = "Book of the",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.onSurface.copy(0.7f),
            modifier = Modifier.padding(horizontal = Dimens.Spacing36)
        )

        Row {
            Text(
                text = "Day",
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

val colors = listOf(
    Color(0xFFD48545),
    Color(0xFFE92E15),
    Color(0xFFDE7F21),
    Color(0xFFE62418),
    Color(0xFFFCEA47),
    Color(0xFFFCEA47),
    Color(0xFFFCEA47),
)

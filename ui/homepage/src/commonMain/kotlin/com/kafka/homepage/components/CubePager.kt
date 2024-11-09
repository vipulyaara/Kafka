package com.kafka.homepage.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.kafka.data.entities.Item
import com.kafka.ui.components.material.HorizontalCubePager
import ui.common.theme.theme.Dimens

@Composable
fun CubePager(
    carouselItems: List<Item>,
    images: List<String>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberPagerState { carouselItems.size }

    Column {
        Header()

        Spacer(Modifier.height(Dimens.Gutter))

        Text(
            text = "Swipe right",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(horizontal = Dimens.Gutter, vertical = Dimens.Spacing08)
                .align(Alignment.End)
        )

        HorizontalCubePager(state = state, modifier = modifier) { page ->
            AsyncImage(
                model = images.getOrNull(page) ?: carouselItems[page].coverImage,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(0.66f)
                    .clickable { onClick(carouselItems[page].itemId) }
            )
        }
    }
}

package org.kafka.homepage.components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import org.kafka.common.CarouselItem
import org.kafka.common.carousels
import org.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens

@Composable
internal fun Carousels(
    carouselItems: List<CarouselItem> = remember { carousels },
    onBannerClick: (String) -> Unit = {},
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    BoxWithConstraints {
        LazyRow(
            modifier = modifier
                .padding(vertical = Dimens.Spacing04)
                .heightIn(max = Dimens.Spacing196),
            contentPadding = PaddingValues(horizontal = Dimens.Spacing12),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            itemsIndexed(carouselItems) { _, item ->
                CarouselItem(item, onBannerClick)
            }
        }
    }
}

@Composable
private fun CarouselItem(item: CarouselItem, onBannerClick: (String) -> Unit) {
    AsyncImage(
        model = item.image,
        contentScale = ContentScale.Fit,
        contentDescription = null,
        modifier = Modifier
            .padding(Dimens.Spacing02)
            .shadowMaterial(
                elevation = Dimens.Spacing12,
                shape = RoundedCornerShape(Dimens.Spacing08)
            )
            .clickable { onBannerClick(item.itemId) }
    )
}

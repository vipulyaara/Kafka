package org.kafka.homepage.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kafka.data.model.homepage.HomepageBanner
import kotlinx.collections.immutable.ImmutableList
import org.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens

@Composable
internal fun Carousels(
    carouselItems: ImmutableList<HomepageBanner>,
    onBannerClick: (HomepageBanner) -> Unit,
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    LazyRow(
        modifier = modifier.padding(vertical = Dimens.Spacing04),
        contentPadding = PaddingValues(horizontal = Dimens.Spacing12),
        verticalAlignment = Alignment.CenterVertically,
        state = lazyListState,
        flingBehavior = rememberSnapFlingBehavior(
            snapLayoutInfoProvider = remember(lazyListState) {
                SnapLayoutInfoProvider(lazyListState = lazyListState)
            },
        )
    ) {
        itemsIndexed(carouselItems) { _, item ->
            CarouselItem(item = item, onBannerClick = onBannerClick)
        }
    }
}

@Composable
private fun CarouselItem(
    item: HomepageBanner,
    onBannerClick: (HomepageBanner) -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(item.imageUrl)
            .crossfade(true)
            .build(),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = modifier
            .widthIn(min = 324.dp)
            .height(184.dp)
            .padding(Dimens.Spacing02)
            .shadowMaterial(
                elevation = Dimens.Spacing12,
                shape = RoundedCornerShape(Dimens.Spacing08)
            )
            .clickable { onBannerClick(item) }
    )
}

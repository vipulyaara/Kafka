package org.kafka.homepage.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    lazyListState: LazyListState = rememberLazyListState(),
) {
    val state = rememberPagerState { carouselItems.size }

    HorizontalPager(
        state = state,
        modifier = modifier
            .padding(Dimens.Spacing02)
            .padding(top = Dimens.Spacing12, bottom = Dimens.Spacing08),
        contentPadding = PaddingValues(horizontal = Dimens.Spacing12),
    ) { index ->
        carouselItems.getOrNull(index)?.let { item ->
            FeaturedItem(
                item = item,
                label = item.title,
                imageUrl = images.getOrNull(index),
                onClick = { onBannerClick(item.itemId) },
                modifier = Modifier.padding(horizontal = Dimens.Spacing04)
            )
        }
    }
}

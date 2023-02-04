package org.kafka.homepage.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.kafka.common.CarouselItem
import org.kafka.common.carousels
import org.kafka.common.shadowMaterial
import org.kafka.common.widgets.LoadImage
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.RootScreen
import ui.common.theme.theme.Dimens

@Composable
internal fun Carousels(
    carouselItems: List<CarouselItem> = carousels,
    modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.current
    val pagerState = rememberPagerState()

    HorizontalPager(
        modifier = modifier.padding(vertical = Dimens.Spacing04),
        pageCount = carouselItems.size,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = Dimens.Spacing12)
    ) {
        val item = carouselItems[it]
        LoadImage(
            data = item.image,
            modifier = Modifier
                .padding(Dimens.Spacing02)
                .heightIn(Dimens.CarouselMinHeight, Dimens.CarouselMaxHeight)
                .fillMaxWidth()
                .shadowMaterial(
                    elevation = Dimens.Spacing12,
                    shape = RoundedCornerShape(Dimens.Spacing08)
                )
                .clickable {
                    navigator.navigate(
                        LeafScreen.ItemDetail.buildRoute(item.itemId, RootScreen.Home)
                    )
                }
        )
    }
}

@Preview
@Composable
private fun CarouselsPreview() {
    Carousels()
}

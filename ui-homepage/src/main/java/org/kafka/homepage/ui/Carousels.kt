package org.kafka.homepage.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    val navigator = LocalNavigator.current
    val pagerState = rememberPagerState()

    HorizontalPager(
        modifier = modifier.padding(vertical = Dimens.Spacing04),
        pageCount = carouselItems.size,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = Dimens.Spacing12)
    ) { page ->
        val item = carouselItems[page]
        LoadImage(
            data = item.image,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(Dimens.Spacing02)
                .fillMaxWidth()
                .shadowMaterial(
                    elevation = Dimens.Spacing12,
                    shape = RoundedCornerShape(Dimens.Spacing08)
                )
                .clickable {
                    if (page == 0) {
                        navigator.navigate(LeafScreen.Search.buildRoute("kafka%20archives"))
                    } else {
                        navigator.navigate(
                            LeafScreen.ItemDetail.buildRoute(item.itemId, RootScreen.Home)
                        )
                    }
                }
        )
    }
}

package org.kafka.homepage.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.kafka.common.banners
import org.kafka.common.shadowMaterial
import org.kafka.common.widgets.LoadImage
import ui.common.theme.theme.Dimens

private val images = banners.subList(0, 4)

@Composable
internal fun Carousels() {
    val pagerState = rememberPagerState()
    HorizontalPager(
        modifier = Modifier.padding(vertical = Dimens.Spacing04),
        count = images.size,
        state = pagerState,
        contentPadding = PaddingValues(horizontal = Dimens.Spacing12)
    ) {
        LoadImage(
            data = images[it],
            modifier = Modifier
                .padding(Dimens.Spacing02)
                .heightIn(Dimens.CarouselMinHeight, Dimens.CarouselMaxHeight)
                .fillMaxWidth()
                .shadowMaterial(
                    elevation = Dimens.Spacing12,
                    shape = RoundedCornerShape(Dimens.Spacing08)
                )
        )
    }
}

@Preview
@Composable
private fun CarouselsPreview() {
    Carousels()
}

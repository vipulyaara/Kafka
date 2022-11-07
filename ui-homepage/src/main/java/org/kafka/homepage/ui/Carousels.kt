package org.kafka.homepage.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import org.kafka.common.banners
import org.kafka.common.shadowMaterial
import org.kafka.common.widgets.LoadImage

private val images = banners.subList(0, 4)

@Composable
internal fun Carousels() {
    val pagerState = rememberPagerState()
    HorizontalPager(
        modifier = Modifier.padding(vertical = 4.dp),
        count = images.size,
        state = pagerState,
        contentPadding = PaddingValues(end = 24.dp)
    ) {
        LoadImage(
            data = images[it],
            modifier = Modifier
                .padding(2.dp)
                .heightIn(124.dp, 188.dp)
                .fillMaxWidth()
                .shadowMaterial(elevation = 12.dp, shape = RoundedCornerShape(8.dp))
        )
    }
}

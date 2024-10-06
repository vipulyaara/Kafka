@file:OptIn(ExperimentalFoundationApi::class)

package com.kafka.ui.components.material

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import ui.common.theme.theme.Dimens
import kotlin.math.absoluteValue

@Composable
fun CircleIndicator(state: PagerState, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        for (i in 0 until state.pageCount) {
            val offset = state.indicatorOffsetForPage(i)

            Box(
                modifier = Modifier.size(Dimens.Spacing16),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .size(lerp(Dimens.Spacing06, Dimens.Spacing16, offset))
                        .border(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = CircleShape,
                        )
                )
            }
        }
    }
}

@Composable
fun SquareIndicator(modifier: Modifier = Modifier, state: PagerState) {
    Row(
        modifier = modifier.height(40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        for (i in 0 until state.pageCount) {
            Box(
                modifier = Modifier.size(20.dp),
                contentAlignment = Alignment.Center
            ) {
                val offset = state.indicatorOffsetForPage(i)
                Box(
                    Modifier
                        .rotate(135f * offset)
                        .size(lerp(10.dp, 16.dp, offset))
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = RectangleShape,
                        )
                )
            }
        }
    }
}

fun PagerState.offsetForPage(page: Int) = (currentPage - page) + currentPageOffsetFraction

fun PagerState.indicatorOffsetForPage(page: Int) =
    1f - offsetForPage(page).coerceIn(-1f, 1f).absoluteValue

package com.kafka.root.home.bottombar

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.kafka.common.simpleClickable
import com.kafka.navigation.graph.RootScreen
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@ExperimentalAnimationApi
@Composable
internal fun BottomNav(
    screens: List<HomeNavigationItem>,
    selectedScreen: RootScreen,
    modifier: Modifier = Modifier,
    select: (RootScreen) -> Unit,
) {
    val colors = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.tertiary,
        MaterialTheme.colorScheme.secondary,
        MaterialTheme.colorScheme.error,
    )

    val selectedIndex = screens.indexOfFirst { it.rootScreen == selectedScreen }
    val animatedColor by animateColorAsState(colors[selectedIndex])
    val animatedSelectedIndex by animateFloatAsState(
        targetValue = selectedIndex.toFloat(),
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )

    Box(
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        Column {
            Box(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                GleemIndicator(
                    screens = screens,
                    animatedColor = animatedColor,
                    animatedSelectedIndex = animatedSelectedIndex
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (screen in screens) {
                    val selected = screen.rootScreen == selectedScreen
                    val animatedWeight by animateFloatAsState(targetValue = if (selected) 1.5f else 1f)
                    val index = screens.indexOf(screen)

                    Box(
                        modifier = Modifier.weight(animatedWeight),
                        contentAlignment = Alignment.Center,
                    ) {
                        BottomNavItem(
                            modifier = Modifier.simpleClickable { select(screen.rootScreen) },
                            item = screen,
                            color = animatedColor,
                            selected = selected,
                            totalTabs = screens.size,
                            index = index
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GleemIndicator(
    screens: List<HomeNavigationItem>,
    animatedColor: Color,
    animatedSelectedIndex: Float
) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Dimens.Spacing16)
            .height(1.dp)
    ) {
        val tabWidth = size.width / screens.size

        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    animatedColor.copy(alpha = 0f),
                    animatedColor.copy(alpha = 0.6f),
                    animatedColor.copy(alpha = 0.6f),
                    animatedColor.copy(alpha = 0f),
                ),
                startX = tabWidth * animatedSelectedIndex,
                endX = tabWidth * (animatedSelectedIndex + 1),
            ),
            start = Offset(tabWidth * animatedSelectedIndex, 0f),
            end = Offset(tabWidth * (animatedSelectedIndex + 1), 0f),
            strokeWidth = 2.dp.toPx()
        )
    }
}

@ExperimentalAnimationApi
@Composable
private fun BottomNavItem(
    modifier: Modifier = Modifier,
    item: HomeNavigationItem,
    selected: Boolean,
    color: Color,
    totalTabs: Int,
    index: Int
) {
    val animatedElevation by animateDpAsState(targetValue = if (selected) 12.dp else 0.dp)
    val animatedAlpha by animateFloatAsState(
        targetValue = if (selected) 0.6f else 0f,
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )
    val animatedIndex by animateFloatAsState(
        targetValue = index.toFloat(),
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
                .blur(50.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
        ) {
            val tabWidth = size.width / totalTabs
            drawCircle(
                color = color.copy(alpha = animatedAlpha),
                radius = size.height / 2,
                center = Offset(
                    (tabWidth * animatedIndex) + tabWidth / 2,
                    size.height / 2
                )
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = Dimens.Spacing24)
                .shadow(
                    elevation = animatedElevation,
                    shape = RoundedCornerShape(20.dp),
                    ambientColor = color,
                    spotColor = color
                )
                .then(
                    if (selected) {
                        Modifier.background(
                            color = MaterialTheme.colorScheme.inverseSurface,
                            shape = RoundedCornerShape(20.dp)
                        )
                    } else {
                        Modifier
                    }
                )
        ) {
            AnimatedContent(selected) { selectedState ->
                if (selectedState) {
                    Text(
                        text = stringResource(item.labelResId),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.inverseOnSurface,
                        maxLines = 1,
                        modifier = Modifier.padding(
                            horizontal = Dimens.Spacing16,
                            vertical = Dimens.Spacing08
                        ),
                    )
                } else {
                    Icon(
                        painter = rememberVectorPainter(image = item.iconImageVector),
                        contentDescription = stringResource(item.contentDescriptionResId),
                    )
                }
            }
        }
    }
}

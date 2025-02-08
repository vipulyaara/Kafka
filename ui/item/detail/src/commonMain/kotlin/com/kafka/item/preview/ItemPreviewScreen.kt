@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.kafka.item.preview

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.animation.LocalAnimatedContentScope
import com.kafka.common.animation.LocalSharedTransitionScope
import com.kafka.common.simpleClickable
import com.kafka.data.entities.Item
import com.kafka.navigation.LocalNavigator
import com.kafka.navigation.graph.Screen
import com.kafka.navigation.graph.Screen.ItemDetail.SharedElementCoverKey
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.gradients.InfiniteGradient
import com.kafka.ui.components.item.CoverImage
import com.sarahang.playback.ui.color.DynamicTheme
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.LocalTheme
import ui.common.theme.theme.isDark

@Composable
fun ItemPreviewScreen(viewModel: ItemPreviewViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(containerColor = Color.Transparent) { padding ->
        ProvideScaffoldPadding(padding) {
            if (state.item != null) {
                ItemPreview(item = state.item!!, origin = state.origin)
            }
        }
    }
}

@Composable
fun ItemPreview(item: Item, origin: Screen.ItemDetail.Origin) {
    Box(modifier = Modifier.fillMaxSize()) {
        DynamicTheme(model = item.coverImage, useDarkTheme = LocalTheme.current.isDark()) {
            InfiniteGradient(modifier = Modifier.fillMaxSize())
        }
        CoverImage(item = item, origin = origin)
    }
}

@Composable
private fun CoverImage(item: Item, origin: Screen.ItemDetail.Origin) {
    val navigator = LocalNavigator.current

    with(LocalSharedTransitionScope.current) {
        CoverImage(
            data = item.coverImage,
            shape = RoundedCornerShape(Dimens.Radius08),
            placeholder = null,
            modifier = Modifier
                .fillMaxSize()
                .aspectRatio(0.56f)
                .simpleClickable { navigator.goBack() }
                .padding(Dimens.Gutter)
                .sharedElement(
                    state = rememberSharedContentState(
                        key = SharedElementCoverKey(cover = item.coverImage, origin = origin)
                    ),
                    animatedVisibilityScope = LocalAnimatedContentScope.current
                )
        )
    }
}

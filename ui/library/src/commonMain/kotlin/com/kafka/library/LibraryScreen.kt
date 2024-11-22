package com.kafka.library

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafka.library.favorites.FavoriteViewModel
import com.kafka.library.favorites.Favorites
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.ScribbleTabs
import com.kafka.ui.components.scaffoldPadding
import ui.common.theme.theme.Dimens

@Composable
fun LibraryScreen(favoriteViewModel: FavoriteViewModel) {
    Scaffold { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = scaffoldPadding().calculateTopPadding())
            ) {
                LibraryContent(favoriteViewModel)
            }
        }
    }
}

@Composable
private fun LibraryContent(favoriteViewModel: FavoriteViewModel) {
    val tabs = listOf("Wishlist", "Uploads")
    val state = rememberPagerState { tabs.size }

    Column {
        Spacer(Modifier.height(Dimens.Spacing56))

        ScribbleTabs(tabs = tabs, pagerState = state)

        HorizontalPager(state = state) { page ->
            when (page) {
                0 -> Favorites(favoriteViewModel)
                else -> Box(Modifier.fillMaxSize())
            }
        }
    }
}

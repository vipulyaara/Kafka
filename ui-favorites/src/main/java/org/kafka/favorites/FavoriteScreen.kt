package org.kafka.favorites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.kafka.common.asImmutable
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.LocalNavigator
import org.kafka.navigation.RootScreen
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.item.LayoutType

@Composable
fun FavoriteScreen(viewModel: FavoriteViewModel = hiltViewModel()) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()

    val navigator = LocalNavigator.current
    val openItemDetail: (String) -> Unit = {
        navigator.navigate(LeafScreen.ItemDetail.buildRoute(it, RootScreen.Library))
    }

    Scaffold { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Favorites(viewState, { viewModel.updateLayoutType(it) }, openItemDetail)
        }
    }
}

@Composable
private fun Favorites(
    viewState: FavoriteViewState,
    changeLayoutType: (LayoutType) -> Unit,
    openItemDetail: (String) -> Unit
) {
    val pagerState = rememberPagerState()

    Column(modifier = Modifier.fillMaxSize()) {
        Tabs(
            pagerState = pagerState,
            tabs = LibraryTab.values().map { it.name }.asImmutable(),
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            pageCount = LibraryTab.values().size,
            state = pagerState
        ) { page ->
            Column {
                when (val libraryTab = LibraryTab.values()[page]) {
                    LibraryTab.Favorites -> LibraryItems(
                        items = viewState.favoriteItems,
                        libraryTab = libraryTab,
                        layoutType = viewState.layoutType,
                        changeLayoutType = changeLayoutType,
                        openItemDetail = openItemDetail
                    )

                    LibraryTab.Downloads -> LibraryItems(
                        items = viewState.downloadedItems,
                        libraryTab = libraryTab,
                        layoutType = viewState.layoutType,
                        changeLayoutType = changeLayoutType,
                        openItemDetail = openItemDetail
                    )
                }
            }
        }
    }
}

internal enum class LibraryTab { Favorites, Downloads }

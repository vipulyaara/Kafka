package com.kafka.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toPersistentList
import com.kafka.library.downloads.Downloads
import com.kafka.library.downloads.DownloadsViewModel
import com.kafka.library.favorites.FavoriteViewModel
import com.kafka.library.favorites.Favorites
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.scaffoldPadding

@Composable
fun LibraryScreen(favoriteViewModel: FavoriteViewModel, downloadsViewModel: DownloadsViewModel) {
    Scaffold { padding ->
        ProvideScaffoldPadding(padding = padding) {
            val pagerState = rememberPagerState(pageCount = { LibraryTab.entries.size })

            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.currentPage }.collect { page ->
                    if (page == 1) {
                        downloadsViewModel.logDownloadPageOpen()
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = scaffoldPadding().calculateTopPadding())
            ) {
                Tabs(
                    pagerState = pagerState,
                    tabs = LibraryTab.entries.map { it.name }.toPersistentList(),
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalPager(modifier = Modifier.fillMaxSize(), state = pagerState) { page ->
                    when (LibraryTab.entries[page]) {
                        LibraryTab.Favorites -> Favorites(favoriteViewModel)
                        LibraryTab.Downloads -> Downloads(downloadsViewModel)
                    }
                }
            }
        }
    }
}

internal enum class LibraryTab { Favorites, Downloads }

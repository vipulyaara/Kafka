package org.kafka.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.collections.immutable.toPersistentList
import org.kafka.library.downloads.Downloads
import org.kafka.library.favorites.Favorites
import org.kafka.ui.components.ProvideScaffoldPadding
import org.kafka.ui.components.scaffoldPadding

@Composable
fun LibraryScreen() {
    Scaffold { padding ->
        ProvideScaffoldPadding(padding = padding) {
            val pagerState = rememberPagerState(pageCount = { LibraryTab.entries.size })

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
                        LibraryTab.Favorites -> Favorites()
                        LibraryTab.Downloads -> Downloads()
                    }
                }
            }
        }
    }
}

internal enum class LibraryTab { Favorites, Downloads }

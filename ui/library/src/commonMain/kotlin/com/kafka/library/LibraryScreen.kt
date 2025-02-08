package com.kafka.library

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafka.library.bookshelf.BookshelfDetailViewModel
import com.kafka.library.bookshelf.BookshelfItems
import com.kafka.library.bookshelf.LibraryViewModel
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.ScribbleTabs

@Composable
fun LibraryScreen(
    bookshelfFactory: () -> LibraryViewModel,
    detailFactory: (String) -> BookshelfDetailViewModel
) {
    val bookshelvesViewModel = viewModel { bookshelfFactory() }
    val bookshelves by bookshelvesViewModel.bookshelves.collectAsStateWithLifecycle()
    val state = rememberPagerState { bookshelves.size }

    Scaffold(
        topBar = {
            ScribbleTabs(
                tabs = bookshelves.map { it.name },
                pagerState = state,
                modifier = Modifier.padding(top = 54.dp)
            )
        }
    ) { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Column(modifier = Modifier.fillMaxSize()) {
                HorizontalPager(state = state) { page ->
                    val bookshelf = bookshelves[page]
                    BookshelfItems(bookshelf = bookshelf, detailFactory = detailFactory)
                }
            }
        }
    }
}

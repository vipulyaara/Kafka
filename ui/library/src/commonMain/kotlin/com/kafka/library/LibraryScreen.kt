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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kafka.data.entities.Bookshelf
import com.kafka.library.bookshelf.BookshelfDetailViewModel
import com.kafka.library.bookshelf.BookshelfItems
import com.kafka.library.bookshelf.BookshelvesViewModel
import com.kafka.ui.components.ProvideScaffoldPadding
import com.kafka.ui.components.material.ScribbleTabs
import ui.common.theme.theme.Dimens

@Composable
fun LibraryScreen(
    bookshelfFactory: () -> BookshelvesViewModel,
    detailFactory: (String) -> BookshelfDetailViewModel
) {
    val bookshelvesViewModel = viewModel { bookshelfFactory() }
    val bookshelves by bookshelvesViewModel.bookshelves.collectAsStateWithLifecycle()

    Scaffold { padding ->
        ProvideScaffoldPadding(padding = padding) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Dimens.Spacing56)
            ) {
                val state = rememberPagerState { bookshelves.size }

                Column {
                    ScribbleTabs(tabs = bookshelves.map { it.name }, pagerState = state)

                    HorizontalPager(state = state) { page ->
                        val bookshelf = bookshelves[page]
                        BookshelfItems(bookshelf, detailFactory)
                    }
                }
            }
        }
    }
}

@Composable
private fun BookshelfItems(
    bookshelf: Bookshelf,
    detailFactory: (String) -> BookshelfDetailViewModel
) {
    val detailViewModel = viewModel(key = bookshelf.id) { detailFactory(bookshelf.id) }
    BookshelfItems(detailViewModel)

//    when (bookshelf.type) {
//        Bookshelf.Type.Uploads -> {
//
//        }
//
//        else -> {
//            BookshelfItems(detailViewModel)
//        }
//    }
}

package com.kafka.kms.ui.books

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.data.entities.ItemDetail
import com.kafka.ui.components.item.GridItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen(viewModel: BooksViewModel) {
    val state = viewModel.state
    val clipboardManager = LocalClipboardManager.current

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Library") },
                actions = {
                    // Add copy button in the top bar
                    IconButton(
                        onClick = {
                            val randomIds = state.books
                                .shuffled()
                                .joinToString(", ") { it.itemId }
                            clipboardManager.setText(AnnotatedString(randomIds))
                        },
                        enabled = state.books.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Copy,
                            contentDescription = "Copy random IDs"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                BooksList(books = state.books)
            }
        }
    }
}

@Composable
private fun BooksList(books: List<ItemDetail>) {
    val clipboardManager = LocalClipboardManager.current

    LazyVerticalGrid(
        columns = GridCells.Adaptive(200.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(books) { book ->
            Box {
                GridItem(
                    mediaType = book.mediaType,
                    coverImage = book.coverImage
                )
                IconButton(
                    onClick = { clipboardManager.setText(AnnotatedString(book.itemId)) },
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Copy,
                        contentDescription = "Copy ${book.title} ID"
                    )
                }
            }
        }
    }
}

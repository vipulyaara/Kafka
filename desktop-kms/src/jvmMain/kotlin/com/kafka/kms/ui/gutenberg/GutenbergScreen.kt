package com.kafka.kms.ui.gutenberg

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.kms.components.TextField
import com.kafka.ui.components.progress.InfiniteProgressBar

@Composable
fun GutenbergScreen(viewModel: GutenbergViewModel, modifier: Modifier = Modifier) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Search section
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Search Gutenberg Books",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                TextField(
                    value = viewModel.bookId,
                    onValueChange = { viewModel.bookId = it },
                    onImeAction = { viewModel.fetchBook() },
                    placeholder = "Enter Gutenberg ID",
                    modifier = Modifier.width(200.dp)
                )

                if (state.loading) {
                    InfiniteProgressBar(modifier = Modifier.size(32.dp))
                }
            }

            LaunchedEffect(state.gutenbergBook) {
                if (state.gutenbergBook != null) {
                    viewModel.createRepository(state.gutenbergBook!!)
                }
            }

            // Book details card
            state.gutenbergBook?.let { book ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    book.authors.getOrNull(0)?.name?.let { authorName ->
                        Text(
                            text = "By $authorName",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

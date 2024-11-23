package com.kafka.library.bookshelf.add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.data.entities.Bookshelf
import kafka.ui.library.generated.resources.Res
import kafka.ui.library.generated.resources.bookshelves
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
fun AddToBookshelf(viewModel: AddToBookshelfViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Bookshelves(
        bookshelves = state.bookshelves,
        itemTitle = state.itemDetail?.title.orEmpty(),
        modifier = Modifier.padding(vertical = Dimens.Spacing24),
        addToBookshelf = viewModel::addToBookShelf
    )
}

@Composable
private fun Bookshelves(
    itemTitle: String,
    bookshelves: List<Bookshelf>,
    modifier: Modifier = Modifier,
    addToBookshelf: (Bookshelf) -> Unit
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(Dimens.Radius12)
    ) {
        LazyColumn {
            item {
                Header(title = itemTitle)
            }

            items(bookshelves) { bookshelf ->
                BookshelfItem(bookshelf = bookshelf, added = false) {
                    addToBookshelf(bookshelf)
                }
            }
        }
    }
}

@Composable
private fun Header(title: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth().padding(Dimens.Spacing24),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = stringResource(Res.string.bookshelves),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(Dimens.Spacing02))

            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

//        Surface(color = MaterialTheme.colorScheme.primary, shape = CircleShape) {
//            Icon(
//                imageVector = Icons.Plus,
//                contentDescription = null,
//                modifier = Modifier.padding(Dimens.Spacing12),
//                tint = MaterialTheme.colorScheme.onPrimary
//            )
//        }
    }
}

@Composable
private fun BookshelfItem(bookshelf: Bookshelf, added: Boolean, onClick: (Boolean) -> Unit) {
    val visibility = when (bookshelf.visibility) {
        Bookshelf.Visibility.Public -> "Public"
        Bookshelf.Visibility.Private -> "Private"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(!added) }
            .padding(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing04),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = bookshelf.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(Dimens.Spacing02))

            Text(
                text = visibility,
                style = MaterialTheme.typography.labelMedium,
                color = Color(0x88929292)
            )
        }

        Spacer(Modifier.weight(1f))

        val icon = if (added) Icons.Check else Icons.Plus
        IconButton(icon, onClick = { onClick(!added) })
    }
}

package com.kafka.content.compose

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.kafka.content.ui.detail.ItemDetailViewModel
import com.kafka.content.ui.detail.ItemDetailViewState
import com.kafka.data.entities.ItemDetail
import com.kafka.ui.theme.KafkaTheme
import dev.chrisbanes.accompanist.coil.CoilImage

@Composable
fun ItemDetail(itemId: String) {
    val viewModel: ItemDetailViewModel = viewModel()
    val viewState: State<ItemDetailViewState> = viewModel.stateFlow.collectAsState()

    viewModel.observeItemDetail(itemId)
    viewModel.updateItemDetail(itemId)

    viewState.value.itemDetail?.let { ItemDetail(it) }
}

@Composable
fun ItemDetail(itemDetail: ItemDetail) {
    ItemDetailDescription(itemDetail = itemDetail)
}

@Composable
fun ItemDetailDescription(itemDetail: ItemDetail) {
    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
        Card(
            modifier = Modifier.padding(top = 56.dp).size(196.dp, 256.dp).align(Alignment.CenterHorizontally),
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            CoilImage(data = itemDetail.coverImage.orEmpty(), contentScale = ContentScale.Crop)
        }
        Text(
            modifier = Modifier.padding(top = 24.dp).align(Alignment.CenterHorizontally),
            text = itemDetail.title.toString(),
            style = MaterialTheme.typography.h5,
            color = KafkaTheme.colors.textPrimary
        )
        Text(
            modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally),
            text = itemDetail.creator.toString(),
            style = MaterialTheme.typography.body2,
            color = KafkaTheme.colors.secondary
        )
        Text(
            modifier = Modifier.padding(top = 12.dp).align(Alignment.CenterHorizontally),
            text = itemDetail.description.toString(),
            maxLines = 4,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.caption,
            color = KafkaTheme.colors.textSecondary
        )
    }
}

@Preview
@Composable
fun ItemDetailPreview() {
    KafkaTheme {
        ItemDetail(
            itemDetail = ItemDetail(
                title = "Selected poems of Ghalib",
                creator = "Mirza Ghalib",
                description = "New book as someone reads Ghazals"
            )
        )
    }
}

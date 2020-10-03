package com.kafka.content.compose

import alignCenter
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.ui.tooling.preview.Preview
import com.kafka.content.R
import com.kafka.content.compose.demos.RotatingGlobe
import com.kafka.content.ui.detail.ItemDetailViewModel
import com.kafka.content.ui.detail.ItemDetailViewState
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.extensions.letEmpty
import com.kafka.ui.theme.KafkaTheme
import dev.chrisbanes.accompanist.coil.CoilImage

@ExperimentalLazyDsl
@Composable
fun ItemDetail(itemId: String) {
    val viewModel: ItemDetailViewModel = viewModel()
    val viewState: State<ItemDetailViewState> = viewModel.stateFlow.collectAsState()

    viewModel.observeItemDetail(itemId)
    viewModel.updateItemDetail(itemId)

//    if (viewState.value.isLoading) {
//        Loader()
//    } else {
        viewState.value.itemDetail?.let { ItemDetail(it, viewState.value.itemsByCreator) }
//    }

}

@Composable
fun Loader() {
    Box(modifier = Modifier.size(136.dp), gravity = ContentGravity.Center) {
        val modifier = Modifier.aspectRatio(1f).fillMaxSize().padding(16.dp)
        RotatingGlobe(modifier)
    }
}


@ExperimentalLazyDsl
@Composable
fun ItemDetail(itemDetail: ItemDetail, items: List<Item>? = null) {
    Surface(modifier = Modifier.fillMaxHeight(), color = KafkaTheme.colors.surface.copy(alpha = 0.15f)) {
        LazyColumn(content = {
            item { ItemDetailDescription(itemDetail = itemDetail) }
            item { ItemDetailActions(itemDetail = itemDetail) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            item { Subjects(itemDetail = itemDetail) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            items?.letEmpty {
                item { LabelItemsBy(creator = itemDetail.creator.orEmpty()) }
                items(items) {
                    ContentItem(item = it, onItemClick = {})
                }
            }
        })
    }
}

@Composable
fun ItemDetailDescription(itemDetail: ItemDetail) {
    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
        Card(
            modifier = Modifier.padding(top = 56.dp).size(196.dp, 250.dp).align(Alignment.CenterHorizontally),
            elevation = 8.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            CoilImage(data = itemDetail.coverImage.orEmpty(), contentScale = ContentScale.Crop)
        }
        Text(
            modifier = Modifier.padding(top = 24.dp).align(Alignment.CenterHorizontally),
            text = itemDetail.title.toString(),
            style = MaterialTheme.typography.h5.alignCenter(),
            color = KafkaTheme.colors.textPrimary
        )
        Text(
            modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally),
            text = itemDetail.creator.toString(),
            style = MaterialTheme.typography.h6,
            color = KafkaTheme.colors.secondary
        )
        Text(
            modifier = Modifier.padding(top = 12.dp).align(Alignment.CenterHorizontally),
            text = itemDetail.description.toString(),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2.copy(fontWeight = FontWeight.Light).alignCenter(),
            color = KafkaTheme.colors.textPrimary.copy(alpha = 0.8f)
        )
    }
}

@Composable
fun ItemDetailActions(itemDetail: ItemDetail) {
    Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        FloatingActionButton(onClick = {}, backgroundColor = KafkaTheme.colors.background) {
            Icon(vectorResource(id = R.drawable.ic_heart))
        }
        Spacer(modifier = Modifier.width(24.dp))

        FloatingActionButton(onClick = {}, backgroundColor = KafkaTheme.colors.background) {
            Icon(vectorResource(id = R.drawable.ic_share_2))
        }
        Spacer(modifier = Modifier.width(24.dp))

        Button(
            modifier = Modifier.weight(1f),
            onClick = {},
            backgroundColor = KafkaTheme.colors.background,
            shape = RoundedCornerShape(4.dp),
            elevation = 4.dp,
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            Text(
                text = "PLAY",
                style = MaterialTheme.typography.button,
                color = KafkaTheme.colors.textPrimary
            )
        }
    }
}


@Composable
fun LabelItemsBy(creator: String) {
    Text(
        modifier = Modifier.padding(24.dp),
        text = "More by $creator",
        style = MaterialTheme.typography.body2,
        color = KafkaTheme.colors.textSecondary
    )
}

@Composable
fun Subjects(itemDetail: ItemDetail) {
    ScrollableRow {
        itemDetail.metadata?.forEach {
            Surface(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = KafkaTheme.colors.background.copy(alpha = 0f),
                shape = RoundedCornerShape(percent = 50),
                border = BorderStroke(2.dp, KafkaTheme.colors.surface)
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    text = it,
                    style = MaterialTheme.typography.body2,
                    color = KafkaTheme.colors.textSecondary
                )
            }
        }
    }
}

@ExperimentalLazyDsl
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

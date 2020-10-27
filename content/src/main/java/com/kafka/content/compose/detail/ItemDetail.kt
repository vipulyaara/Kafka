package com.kafka.content.compose.detail

import androidx.compose.animation.animate
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Icon
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.kafka.content.R
import com.kafka.content.compose.Actions
import com.kafka.content.compose.NetworkImage
import com.kafka.content.compose.item.ContentItem
import com.kafka.content.ui.detail.ItemDetailViewModel
import com.kafka.content.ui.detail.ItemDetailViewState
import com.kafka.content.ui.player.PlayerViewModel
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.isAudio
import com.kafka.data.entities.readerUrl
import com.kafka.data.extensions.letEmpty
import com.kafka.ui_common.extensions.alignCenter
import com.kafka.ui_common.extensions.decrementTextSize
import com.kafka.ui_common.theme.KafkaColors
import com.kafka.ui_common.theme.KafkaTheme
import com.kafka.ui_common.widget.FullScreenLoader

@ExperimentalLazyDsl
@Composable
fun ItemDetail(itemId: String, actions: Actions) {
    val itemDetailViewModel: ItemDetailViewModel = viewModel()
    val playerViewModel: PlayerViewModel = viewModel()
    val itemDetailViewState by itemDetailViewModel.state.collectAsState()

    remember(itemId) {
        itemDetailViewModel.observeItemDetail(itemId)
        itemDetailViewModel.updateItemDetail(itemId)
    }
    val context = ContextAmbient.current

    itemDetailViewState.itemDetail?.takeIf { itemId == it.itemId }?.let {
        ItemDetail(
            itemDetailViewState,
            actions,
            ItemDetailActions(
                onFavoriteClick = { itemDetailViewModel.updateFavorite() },
                onPlayClicked = {
                    itemDetailViewModel.addRecentItem()
                    playerViewModel.play(it.itemId)
                },
                onReadClicked = {
                    itemDetailViewModel.addRecentItem()
                    itemDetailViewModel.read(context, it.readerUrl(), "")
                }
            ))
    } ?: FullScreenLoader()
}

@ExperimentalLazyDsl
@Composable
fun ItemDetail(itemDetailViewState: ItemDetailViewState, actions: Actions, itemDetailActions: ItemDetailActions) {
    val itemDetail = itemDetailViewState.itemDetail!!
    val relatedItems = itemDetailViewState.itemsByCreator
    Surface(
        modifier = Modifier.fillMaxHeight(),
        color = KafkaColors.background
    ) {
        LazyColumn(content = {
            item { ItemDetailDescription(itemDetail = itemDetail) }
            item {
                ActionsRow(
                    itemDetail = itemDetail,
                    isFavorite = itemDetailViewState.isFavorite,
                    itemDetailActions = itemDetailActions
                )
            }
            item { Spacer(modifier = Modifier.height(32.dp)) }
            item { Subjects(itemDetail = itemDetail) }
            item { Spacer(modifier = Modifier.height(24.dp)) }
            relatedItems?.letEmpty {
                item { LabelItemsBy(creator = itemDetail.creator.orEmpty()) }
                items(relatedItems) {
                    ContentItem(item = it, onItemClick = { actions.itemDetail(it.itemId) })
                }
            }
        })
    }
}

@Composable
fun ItemDetailDescription(itemDetail: ItemDetail) {
    Column(modifier = Modifier.padding(24.dp).fillMaxWidth()) {
        Card(
            modifier = Modifier.padding(top = 24.dp).size(196.dp, 250.dp).align(Alignment.CenterHorizontally),
            elevation = 24.dp,
            shape = RoundedCornerShape(4.dp)
        ) {
            NetworkImage(data = itemDetail.coverImage.orEmpty())
        }
        Text(
            modifier = Modifier.padding(top = 32.dp).align(Alignment.CenterHorizontally),
            text = itemDetail.title.toString(),
            style = MaterialTheme.typography.h5.alignCenter(),
            color = KafkaColors.textPrimary
        )
        Text(
            modifier = Modifier.padding(top = 4.dp).align(Alignment.CenterHorizontally),
            text = itemDetail.creator.toString(),
            style = MaterialTheme.typography.h6.decrementTextSize().alignCenter(),
            color = KafkaColors.secondary
        )
        Text(
            modifier = Modifier.padding(top = 20.dp).align(Alignment.CenterHorizontally),
            text = RatingText(rating = 3) + AnnotatedString(itemDetail.description.orEmpty()),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.body2,
            color = KafkaColors.textPrimary.copy(alpha = 0.8f)
        )
    }
}

@Composable
private fun ActionsRow(itemDetail: ItemDetail, isFavorite: Boolean, itemDetailActions: ItemDetailActions) {
    val favoriteBackgroundColor = animate(if (isFavorite) Color(0xFFff006a) else KafkaTheme.colors.surface)
    val favoriteIconColor = if (isFavorite) Color(0xFFFFFFFF) else KafkaTheme.colors.textPrimary

    Row(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
        FloatingActionButton(
            modifier = Modifier.size(52.dp),
            onClick = { itemDetailActions.onFavoriteClick() },
            elevation = 24.dp,
            backgroundColor = favoriteBackgroundColor
        ) {
            Icon(vectorResource(id = R.drawable.ic_heart), tint = favoriteIconColor)
        }
        Spacer(modifier = Modifier.width(24.dp))

        FloatingActionButton(
            modifier = Modifier.size(52.dp),
            onClick = { },
            elevation = 24.dp,
            backgroundColor = KafkaColors.surface
        ) {
            Icon(vectorResource(id = R.drawable.ic_share_2), tint = KafkaColors.textPrimary)
        }
        Spacer(modifier = Modifier.width(24.dp))

        Button(
            modifier = Modifier.weight(1f),
            onClick = {
                if (itemDetail.isAudio()) {
                    itemDetailActions.onPlayClicked()
                } else {
                    itemDetailActions.onReadClicked()
                }
            },
            backgroundColor = KafkaColors.surface,
            shape = RoundedCornerShape(0.dp),
            elevation = 24.dp,
            contentPadding = PaddingValues(all = 16.dp)
        ) {
            Text(
                text = if (itemDetail.isAudio()) {
                    "PLAY"
                } else {
                    "READ"
                },
                style = MaterialTheme.typography.button,
                color = KafkaColors.textPrimary
            )
        }
    }
}


@Composable
fun LabelItemsBy(creator: String) {
    Text(
        modifier = Modifier.padding(24.dp),
        text = "More by $creator",
        style = MaterialTheme.typography.subtitle2,
        color = KafkaColors.textSecondary
    )
}

@Composable
fun Subjects(itemDetail: ItemDetail) {
    ScrollableRow {
        itemDetail.metadata?.forEach {
            Surface(
                modifier = Modifier.padding(horizontal = 8.dp),
                color = KafkaColors.background,
                shape = RoundedCornerShape(percent = 50),
                border = BorderStroke(1.5.dp, KafkaColors.textSecondary.copy(alpha = 0.3f))
            ) {
                Text(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    text = it,
                    style = MaterialTheme.typography.subtitle2.decrementTextSize(),
                    color = KafkaColors.textSecondary
                )
            }
        }
    }
}

@Composable
fun RatingText(rating: Int): AnnotatedString {
    val annotatedString = AnnotatedString.Builder("✪✪✪✪✪  ")
        .apply {
            addStyle(SpanStyle(color = KafkaColors.secondary), 0, rating)
            addStyle(SpanStyle(letterSpacing = TextUnit.Companion.Sp(1.5)), 0, length)
        }
    return annotatedString.toAnnotatedString()
}


data class ItemDetailActions(
    val onFavoriteClick: () -> Unit,
    val onPlayClicked: () -> Unit,
    val onReadClicked: () -> Unit
)

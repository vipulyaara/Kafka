package com.kafka.ui.detail

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.CircleShape
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.Button
import androidx.ui.material.Card
import androidx.ui.material.MaterialTheme
import androidx.ui.material.icons.Icons
import androidx.ui.material.icons.twotone.Favorite
import androidx.ui.material.icons.twotone.FavoriteBorder
import androidx.ui.res.vectorResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.*
import com.kafka.ui.*
import com.kafka.ui.R
import dev.chrisbanes.accompanist.coil.CoilImage
import regularButtonPadding

@Composable
fun ItemDetailView(
    itemDetailViewState: ItemDetailViewState,
    recentItem: RecentItem?,
    actioner: (ItemDetailAction) -> Unit
) {
    val itemDetail = itemDetailViewState.itemDetail
    val showDialog = state { false }
    Stack {
        Column {
            ImageCover(modifier = Modifier.gravity(Alignment.CenterHorizontally), itemDetail = itemDetail)

            Text(
                text = itemDetail?.title ?: "",
                style = MaterialTheme.typography.h2.alignCenter(),
                color = colors().onPrimary,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 20.dp)
                    .padding(top = 24.dp).gravity(Alignment.CenterHorizontally)
            )

            Text(
                text = listOfNotNull(itemDetail?.creator, itemDetail?.collection?.split(",")?.firstOrNull())
                    .joinToString(bulletSymbolWithSpace),
                style = MaterialTheme.typography.h6.alignCenter(),
                color = colors().primary,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp).gravity(Alignment.CenterHorizontally)
            )
            Text(
                text = itemDetail.formattedDescription(),
                maxLines = 3,
                style = MaterialTheme.typography.body1.lineHeight(1.4).alignCenter().decrementTextSize(),
                color = colors().onPrimary.alpha(alpha = 0.8f),
                modifier = Modifier.clickable(onClick = { showDialog.value = true }, indication = null)
                    .padding(horizontal = 16.dp, vertical = 24.dp).fillMaxWidth()
            )

            ActionButtons(itemDetailViewState, actioner)
        }
        DescriptionDialog(showDialog = showDialog, description = itemDetail.formattedDescription())
    }
}

@Composable
fun ImageCover(modifier: Modifier, itemDetail: ItemDetail?) {
    Card(
        modifier = modifier + Modifier.preferredSize(196.dp, 248.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 12.dp,
        color = colors().background
    ) {
        CoilImage(data = itemDetail?.coverImage ?: "", contentScale = ContentScale.Crop)
    }
}

@Composable
fun ActionButtons(itemDetailViewState: ItemDetailViewState, actioner: (ItemDetailAction) -> Unit) {
    val itemDetail = itemDetailViewState.itemDetail
    val isFollowed = itemDetailViewState.isFavorite
    val followedIcon = if (isFollowed) Icons.TwoTone.Favorite else Icons.TwoTone.FavoriteBorder
    val followedBackgroundTint = if (isFollowed) Color(0xFFff006a) else colors().background
    val followedIconTint = if (isFollowed) Color.White else colors().primary

    Row(modifier = Modifier.padding(8.dp).gravity(Alignment.CenterHorizontally)) {
        val modifier = Modifier
            .gravity(Alignment.CenterVertically)
            .padding(12.dp)

        Card(
            border = Border(2.dp, colors().surface),
            elevation = 0.dp,
            shape = CircleShape,
            color = followedBackgroundTint,
            modifier = modifier.clickable(onClick = { actioner(ItemDetailAction.FavoriteClick) }, indication = null)
        ) {
            Icon(
                asset = followedIcon.copy(defaultWidth = 24.dp, defaultHeight = 24.dp),
                tint = followedIconTint,
                modifier = Modifier.padding(16.dp)
            )
        }

        Card(
            border = Border(2.dp, colors().surface),
            elevation = 0.dp,
            shape = CircleShape,
            modifier = modifier,
            color = colors().background
        ) {
            Icon(
                asset = vectorResource(id = R.drawable.ic_download).copy(defaultWidth = 24.dp, defaultHeight = 24.dp),
                tint = colors().primary,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.padding(end = 12.dp))

        if (itemDetail.isText()) {
            ButtonItemBlue(
                modifier = Modifier.weight(0.5f).gravity(Alignment.CenterVertically),
                text = "READ",
                icon = R.drawable.ic_layers,
                actioner = { actioner(ItemDetailAction.Read()) })
        }

        if (itemDetail.isAudio()) {
            ButtonItemBlue(
                modifier = Modifier.weight(0.5f).gravity(Alignment.CenterVertically),
                text = "PLAY",
                icon = R.drawable.ic_headphones,
                actioner = { actioner(ItemDetailAction.Play()) })
        }
    }
}

@Composable
fun ButtonItem(modifier: Modifier, text: String, icon: Int, actioner: () -> Unit) {
    Row(modifier = modifier) {
        Button(
            modifier = Modifier.padding(horizontal = 8.dp),
            backgroundColor = Color(0xFFFFFFFF),
            elevation = 0.dp,
            border = Border(2.dp, colors().surface),
            shape = RoundedCornerShape(6.dp),
            onClick = actioner,
            padding = regularButtonPadding
        ) {
            Stack(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.gravity(Alignment.Center)) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.button.alignCenter(),
                        modifier = Modifier,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun ButtonItemBlue(modifier: Modifier, text: String, icon: Int, actioner: () -> Unit) {
    Row(modifier = modifier) {
        Button(
            modifier = Modifier.padding(horizontal = 8.dp),
            backgroundColor = colors().primary,
            elevation = 1.dp,
            shape = RoundedCornerShape(5.dp),
            contentColor = Color.White,
            onClick = actioner,
            padding = regularButtonPadding
        ) {
            Stack(modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.gravity(Alignment.Center)) {
                    VectorImage(
                        id = icon,
                        modifier = Modifier.padding(end = 16.dp),
                        size = 20.dp,
                        tint = Color.White
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.button.alignCenter(),
                        modifier = Modifier,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
fun DescriptionDialog(showDialog: MutableState<Boolean>, description: String) {
    if (showDialog.value) {
        Dialog(onCloseRequest = { showDialog.value = false }) {
            Card(
                modifier = Modifier.padding(vertical = 64.dp).fillMaxWidth(),
                color = colors().background,
                shape = RoundedCornerShape(12.dp),
                elevation = 0.dp
            ) {
                VerticalScroller {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.body2.lineHeight(1.4).alignCenter().justify(),
                        color = colors().onPrimary.alpha(alpha = 0.7f),
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        }
    }
}

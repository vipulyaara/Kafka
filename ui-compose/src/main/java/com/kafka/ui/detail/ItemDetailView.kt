package com.kafka.ui.detail

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Alignment
import androidx.ui.core.ContentScale
import androidx.ui.core.Modifier
import androidx.ui.foundation.Dialog
import androidx.ui.foundation.Text
import androidx.ui.foundation.VerticalScroller
import androidx.ui.foundation.clickable
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.Color
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.*
import androidx.ui.material.ripple.ripple
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.*
import com.kafka.ui.*
import com.kafka.ui.R
import dev.chrisbanes.accompanist.coil.CoilImage
import regularButtonPadding

@Composable
fun ItemDetailView(itemDetail: ItemDetail?, recentItem: RecentItem?, actioner: (ItemDetailAction) -> Unit) {
    val showDialog = state { false }
    Stack {
        Column {
            ImageCover(modifier = Modifier.gravity(Alignment.CenterHorizontally), itemDetail = itemDetail)

            Text(
                text = itemDetail?.title ?: "",
                style = MaterialTheme.typography.h2.alignCenter(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.paddingHV(horizontal = 20.dp)
                    .padding(top = 24.dp).gravity(Alignment.CenterHorizontally)
            )

            Text(
                text = listOfNotNull(itemDetail?.creator, itemDetail?.collection?.split(",")?.firstOrNull())
                    .joinToString(bulletSymbol),
                style = MaterialTheme.typography.h6.alignCenter().copy(color = colors().secondary),
                modifier = Modifier.paddingHV(
                    horizontal = 20.dp,
                    vertical = 2.dp
                ) + Modifier.gravity(Alignment.CenterHorizontally)
            )
            Text(
                text = itemDetail.formattedDescription(),
                maxLines = 3,
                style = MaterialTheme.typography.body1
                    .lineHeight(1.4).alignCenter().alpha(alpha = 0.8f).decrementTextSize(),
                modifier = Modifier.clickable(onClick = { showDialog.value = true }).ripple(enabled = false)
                    .paddingHV(horizontal = 16.dp, vertical = 24.dp).fillMaxWidth()
            )

            ActionButtons(itemDetail, actioner)
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
fun ActionButtons(itemDetail: ItemDetail?, actioner: (ItemDetailAction) -> Unit) {
    Row(modifier = Modifier.padding(8.dp).gravity(Alignment.CenterHorizontally)) {
        ProvideEmphasis(emphasis = EmphasisAmbient.current.disabled) {
            if (itemDetail.hasText()) {
                ButtonItem(
                    modifier = Modifier.weight(0.5f),
                    text = "READ",
                    icon = R.drawable.ic_layers,
                    actioner = { actioner(ItemDetailAction.Read()) })
            }
        }

        if (itemDetail.hasAudio()) {
            ButtonItem(
                modifier = Modifier.weight(0.5f),
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
            modifier = Modifier.paddingHV(horizontal = 8.dp),
            backgroundColor = Color(0xFF3D84FD),
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
                modifier = Modifier.paddingHV(vertical = 64.dp).fillMaxWidth(),
                color = colors().background,
                shape = RoundedCornerShape(12.dp),
                elevation = 0.dp
            ) {
                VerticalScroller {
                    Text(
                        text = description,
                        style = MaterialTheme.typography.body2.lineHeight(1.4).alignCenter().justify(),
                        modifier = Modifier.padding(24.dp)
                    )
                }
            }
        }
    }
}

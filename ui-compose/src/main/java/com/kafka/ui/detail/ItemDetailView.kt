package com.kafka.ui.detail

import androidx.compose.Composable
import androidx.compose.MutableState
import androidx.compose.state
import androidx.ui.core.Modifier
import androidx.ui.foundation.*
import androidx.ui.foundation.shape.corner.RoundedCornerShape
import androidx.ui.graphics.ScaleFit
import androidx.ui.layout.*
import androidx.ui.layout.ColumnScope.gravity
import androidx.ui.material.*
import androidx.ui.material.ripple.RippleEffectFactory
import androidx.ui.res.imageResource
import androidx.ui.text.style.TextOverflow
import androidx.ui.unit.dp
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.RecentItem
import com.kafka.data.entities.formattedDescription
import com.kafka.data.extensions.getRandomAuthorResource
import com.kafka.ui.*
import com.kafka.ui.widget.regularButtonPadding

@Composable
fun ContentDetailItem(itemDetail: ItemDetail?, recentItem: RecentItem?, actioner: (ItemDetailAction) -> Unit) {
    val showDialog = state { false }
    Stack {
        Column {
            Column(modifier = Modifier.gravity(ColumnAlign.Center)) {
                ImageCover(modifier = Modifier.gravity(ColumnAlign.Center), itemDetail = itemDetail)
            }

            Text(
                text = itemDetail?.title ?: "",
                style = MaterialTheme.typography.h2.alignCenter(),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.paddingHV(horizontal = 20.dp).padding(top = 24.dp) + Modifier.gravity(ColumnAlign.Center)
            )

            Text(
                text = "by " + itemDetail?.creator,
                style = MaterialTheme.typography.h3.alignCenter(),
                modifier = Modifier.paddingHV(
                    horizontal = 20.dp,
                    vertical = 2.dp
                ) + Modifier.gravity(ColumnAlign.Center)
            )
            Clickable(onClick = { showDialog.value = true }) {
                Text(
                    text = itemDetail.formattedDescription(),
                    maxLines = 3,
                    style = MaterialTheme.typography.body2.lineHeight(1.4).alignCenter(),
                    modifier = Modifier.paddingHV(horizontal = 16.dp, vertical = 24.dp)
                )
            }

            ActionButtons(actioner)
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
        val image =
            if (itemDetail?.coverImageResource != 0) itemDetail?.coverImageResource else getRandomAuthorResource()
        Image(
            asset = imageResource(id = image ?: getRandomAuthorResource()),
            scaleFit = ScaleFit.FillHeight
        )
    }
}

@Composable
fun ActionButtons(actioner: (ItemDetailAction) -> Unit) {
    Row(modifier = Modifier.padding(16.dp).gravity(ColumnAlign.Center)) {
        ProvideEmphasis(emphasis = EmphasisAmbient.current.disabled) {
            Button(
                modifier = Modifier.weight(0.49f),
                backgroundColor = MaterialTheme.colors.surface,
                shape = RoundedCornerShape(2.dp),
                contentColor = MaterialTheme.colors.onSecondary,
                elevation = 24.dp,
                border = Border(0.dp, colors().background),
                innerPadding = regularButtonPadding,
                onClick = {}
            ) {
                Text(
                    modifier = Modifier.gravity(ColumnAlign.Center).fillMaxWidth(),
                    text = "READ",
                    style = MaterialTheme.typography.button.alignCenter()
                )
            }
        }

        Box(modifier = Modifier.weight(0.04f)) {}

        Button(
            modifier = Modifier.weight(0.49f),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 24.dp,
            border = Border(0.dp, colors().background),
            shape = RoundedCornerShape(2.dp),
            contentColor = MaterialTheme.colors.onPrimary,
            onClick = {
                actioner(ItemDetailAction.Play())
            },
            innerPadding = regularButtonPadding
        ) {
            Text(
                text = "PLAY",
                style = MaterialTheme.typography.button.alignCenter(),
                modifier = Modifier.gravity(RowAlign.Center).fillMaxWidth()
            )
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

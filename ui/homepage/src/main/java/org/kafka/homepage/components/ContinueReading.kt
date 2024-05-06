package org.kafka.homepage.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kafka.data.entities.RecentItem
import com.kafka.data.entities.RecentItemWithProgress
import kotlinx.collections.immutable.ImmutableList
import org.kafka.common.image.Icons
import org.kafka.common.simpleClickable
import org.kafka.common.widgets.shadowMaterial
import org.kafka.homepage.R
import org.kafka.ui.components.LabelMedium
import org.kafka.ui.components.item.ItemCreatorSmall
import org.kafka.ui.components.item.ItemDescription
import org.kafka.ui.components.item.ItemMediaType
import org.kafka.ui.components.item.ItemTitleSmall
import ui.common.theme.theme.Dimens

@Composable
internal fun ContinueReading(
    readingList: ImmutableList<RecentItemWithProgress>,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit,
    removeRecentItem: (String) -> Unit,
    openRecentItems: () -> Unit
) {
    val showAllAction = remember(readingList) { readingList.size > 2 }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .simpleClickable { if (showAllAction) openRecentItems() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabelMedium(
                text = stringResource(id = R.string.continue_reading),
                modifier = Modifier.padding(horizontal = Dimens.Gutter)
            )

            if (showAllAction) {
                TextButton(
                    onClick = openRecentItems,
                    modifier = Modifier.padding(end = Dimens.Spacing04)
                ) {
                    Text(
                        text = stringResource(R.string.see_all),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(Dimens.Spacing40))
            }
        }

        LazyRow(
            contentPadding = PaddingValues(
                start = Dimens.Spacing12,
                end = Dimens.Spacing12,
                bottom = Dimens.Spacing12
            ),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing20)
        ) {
            items(readingList, key = { it.recentItem.itemId }) { continueReading ->
                ContinueReadingItem(
                    item = continueReading,
                    onItemClicked = { openItemDetail(continueReading.recentItem.itemId) },
                    onItemRemoved = { removeRecentItem(it) }
                )
            }
        }
    }
}

@Composable
private fun ContinueReadingItem(
    item: RecentItemWithProgress,
    modifier: Modifier = Modifier,
    onItemRemoved: (String) -> Unit,
    onItemClicked: () -> Unit
) {
    var isInEditMode by remember { mutableStateOf(false) }
    val recentItem by remember { derivedStateOf { item.recentItem } }

    Box(modifier = modifier
        .clip(RoundedCornerShape(Dimens.Spacing08))
        .combinedClickable(
            onLongClick = { isInEditMode = !isInEditMode },
            onClickLabel = item.recentItem.title,
            onClick = {
                isInEditMode = false
                onItemClicked()
            }
        )
    ) {
        Column(modifier = Modifier.widthIn(50.dp, 286.dp)) {
            Row(
                modifier = Modifier.padding(Dimens.Spacing08),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing16)
            ) {
                CoverImage(recentItem)

                ItemDescription(
                    title = { ItemTitleSmall(recentItem.title, 1) },
                    creator = { ItemCreatorSmall(recentItem.creator) },
                    mediaType = { ItemMediaType(recentItem.mediaType) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(Dimens.Spacing12))

            ShelfWithProgress(progress = item.progress)
        }

        RemoveRecentItemButton(isInEditMode, onItemRemoved, recentItem)
    }
}

@Composable
private fun ShelfWithProgress(modifier: Modifier = Modifier, progress: Float) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.Spacing12)
            .shadowMaterial(Dimens.Spacing12, RoundedCornerShape(Dimens.Spacing12))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
        shape = RoundedCornerShape(Dimens.Spacing12),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    ) {
        Spacer(modifier = Modifier.fillMaxSize())
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress)
        )
    }
}

@Composable
private fun BoxScope.RemoveRecentItemButton(
    isInEditMode: Boolean,
    onItemRemoved: (String) -> Unit,
    continueReading: RecentItem
) {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = .90f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(230, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    if (isInEditMode) {
        IconButton(
            modifier = Modifier
                .size(Dimens.Spacing44)
                .align(Alignment.TopEnd)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                },
            onClick = { onItemRemoved(continueReading.fileId) },
        ) {
            Icon(
                imageVector = Icons.XCircle,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(R.string.cd_remove_from_reading_list)
            )
        }
    }
}

@Composable
private fun CoverImage(item: RecentItem) {
    Box(
        modifier = Modifier
            .shadowMaterial(
                elevation = Dimens.Spacing08,
                shape = RoundedCornerShape(Dimens.Spacing04)
            )
    ) {
        AsyncImage(
            model = item.coverUrl,
            contentDescription = null,
            modifier = Modifier
                .size(64.dp, 76.dp)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
    }
}

// todo add progress
@Composable
private fun Progress(progress: Float) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .height(Dimens.Spacing04)
                .width(116.dp)
                .clip(RoundedCornerShape(50))
        )
        Text(
            text = "20%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}


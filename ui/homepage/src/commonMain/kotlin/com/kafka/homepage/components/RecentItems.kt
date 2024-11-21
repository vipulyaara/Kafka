@file:OptIn(ExperimentalFoundationApi::class)

package com.kafka.homepage.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.common.simpleClickable
import com.kafka.common.widgets.shadowMaterial
import com.kafka.data.entities.RecentItem
import com.kafka.ui.components.LabelMedium
import com.kafka.ui.components.item.CoverImage
import com.kafka.ui.components.item.ItemCreatorSmall
import com.kafka.ui.components.item.ItemDescription
import com.kafka.ui.components.item.ItemMediaType
import com.kafka.ui.components.item.ItemTitleSmall
import com.kafka.ui.components.item.PerspectiveShapeWithShadow
import kafka.ui.homepage.generated.resources.Res
import kafka.ui.homepage.generated.resources.cd_remove_from_reading_list
import kafka.ui.homepage.generated.resources.continue_reading
import kafka.ui.homepage.generated.resources.see_all
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
internal fun RecentItems(
    readingList: List<RecentItem>,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit,
    removeRecentItem: (String) -> Unit,
    openRecentItems: () -> Unit
) {
    val showAllAction = remember(readingList) { readingList.size > 0 }

    Column(modifier = modifier.background(MaterialTheme.colorScheme.surface)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .simpleClickable { if (showAllAction) openRecentItems() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LabelMedium(
                text = stringResource(Res.string.continue_reading),
                modifier = Modifier.padding(horizontal = Dimens.Gutter)
            )

            if (showAllAction) {
                TextButton(
                    onClick = openRecentItems,
                    modifier = Modifier.padding(end = Dimens.Spacing04),
                ) {
                    Text(
                        text = stringResource(Res.string.see_all),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface
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
            items(readingList, key = { it.itemId }) { continueReading ->
                RecentItem(
                    item = continueReading,
                    modifier = Modifier.animateItem(),
                    onItemClicked = { openItemDetail(continueReading.itemId) },
                    onItemRemoved = { removeRecentItem(it) }
                )
            }
        }
    }
}

@Composable
private fun RecentItem(
    item: RecentItem,
    modifier: Modifier = Modifier,
    onItemRemoved: (String) -> Unit,
    onItemClicked: () -> Unit
) {
    var isInEditMode by remember { mutableStateOf(false) }
    val recentItem by remember { derivedStateOf { item } }

    Box(modifier = modifier
        .clip(RoundedCornerShape(Dimens.Spacing08))
        .combinedClickable(
            onLongClick = { isInEditMode = !isInEditMode },
            onClickLabel = item.title,
            onClick = {
                isInEditMode = false
                onItemClicked()
            }
        )
    ) {
        Box(modifier = Modifier.widthIn(50.dp, 286.dp)) {
            ShelfWithProgress(
                progress = item.progress,
                modifier = Modifier.align(Alignment.BottomCenter)
            )

            Row(
                modifier = Modifier
                    .padding(Dimens.Spacing08)
                    .padding(start = Dimens.Spacing12, bottom = Dimens.Spacing12),
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing16)
            ) {
                RecentItemCoverImage(recentItem)

                ItemDescription(
                    title = { ItemTitleSmall(recentItem.title, 1) },
                    creator = { ItemCreatorSmall(recentItem.creator) },
                    mediaType = { ItemMediaType(recentItem.mediaType) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        RemoveRecentItemButton(
            isInEditMode = isInEditMode,
            onItemRemoved = onItemRemoved,
            continueReading = recentItem
        )
    }
}

@Composable
private fun ShelfWithProgress(modifier: Modifier = Modifier, progress: Float) {
    Box(modifier = modifier) {
        PerspectiveShapeWithShadow(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.Spacing56),
            color = MaterialTheme.colorScheme.surfaceContainerLow
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.Spacing12)
                .align(Alignment.BottomCenter),
            shape = RoundedCornerShape(Dimens.Spacing04),
            tonalElevation = Dimens.Elevation04,
            shadowElevation = Dimens.Elevation04,
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Spacer(modifier = Modifier.fillMaxSize())
            Spacer(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress)
            )
        }
    }
}

@Composable
private fun BoxScope.RemoveRecentItemButton(
    isInEditMode: Boolean,
    onItemRemoved: (String) -> Unit,
    continueReading: RecentItem
) {
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = .90f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "scale"
    )
    val rotation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 10f,
        animationSpec = infiniteRepeatable(
            animation = tween(230, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "rotation"
    )

    if (isInEditMode) {
        IconButton(
            modifier = Modifier
                .size(Dimens.Spacing44)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.TopEnd)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                },
            onClick = { onItemRemoved(continueReading.itemId) },
        ) {
            Icon(
                imageVector = Icons.XCircle,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(Res.string.cd_remove_from_reading_list)
            )
        }
    }
}

@Composable
private fun RecentItemCoverImage(item: RecentItem) {
    Box(
        modifier = Modifier
            .shadowMaterial(
                elevation = Dimens.Spacing08,
                shape = RoundedCornerShape(Dimens.Spacing04)
            )
    ) {
        CoverImage(
            data = item.coverUrl,
            contentDescription = null,
            size = DpSize(64.dp, 92.dp),
            containerColor = MaterialTheme.colorScheme.background,
            contentScale = ContentScale.Crop,
            placeholder = null
        )
    }
}

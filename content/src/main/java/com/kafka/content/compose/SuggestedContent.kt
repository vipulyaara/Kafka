package com.kafka.content.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.launchInComposition
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import com.kafka.ui.theme.DynamicThemePrimaryColorsFromImage
import com.kafka.ui.theme.KafkaTheme
import com.kafka.ui.theme.constrastAgainst
import com.kafka.ui.theme.rememberDominantColorState
import com.kafka.ui.widget.Pager
import com.kafka.ui.widget.PagerState
import decrementTextSize

/**
 * This is the minimum amount of calculated constrast for a color to be used on top of the
 * surface color. These values are defined within the WCAG AA guidelines, and we use a value of
 * 3:1 which is the minimum for user-interface components.
 */
private const val MinConstastOfPrimaryVsSurface = 3f

@Composable
fun SuggestedContent(items: List<Item>) {
    val surfaceColor = Color(0xffffffff)
    val dominantColorState = rememberDominantColorState { color ->
        // We want a color which has sufficient contrast against the surface color
        color.constrastAgainst(surfaceColor) >= MinConstastOfPrimaryVsSurface
    }

    DynamicThemePrimaryColorsFromImage(dominantColorState) {
        val clock = AnimationClockAmbient.current
        val pagerState = remember(clock) { PagerState(clock) }

        val selectedImageUrl = items.getOrNull(pagerState.currentPage)?.coverImage

        // When the selected image url changes, call updateColorsFromImageUrl() or reset()
        if (selectedImageUrl != null) {
            launchInComposition(selectedImageUrl) {
                dominantColorState.updateColorsFromImageUrl(selectedImageUrl)
            }
        } else {
            dominantColorState.reset()
        }

        if (items.isNotEmpty()) {
                FollowedPodcasts(
                    items = items,
                    pagerState = pagerState,
                    onPodcastUnfollowed = {},
                    modifier = Modifier
                        .padding(start = 12.dp, top = 16.dp, end = 12.dp)
                        .fillMaxWidth()
                        .preferredHeight(200.dp)
                )
        }
    }
}

private const val PodcastCarouselUnselectedScale = 0.50f

@Composable
fun FollowedPodcasts(
    items: List<Item>,
    pagerState: PagerState = run {
        val clock = AnimationClockAmbient.current
        remember(clock) { PagerState(clock) }
    },
    onPodcastUnfollowed: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    pagerState.maxPage = (items.size - 1).coerceAtLeast(0)

    Pager(
        state = pagerState,
        modifier = modifier
    ) {
        val item = items[page]
        SuggestedItem(
            item = item,
            modifier = Modifier.padding(4.dp)
                .fillMaxHeight()
                .scalePagerItems(unselectedScale = PodcastCarouselUnselectedScale),
            onItemClick = {}
        )
    }
}

@Composable
fun SuggestedItem(item: Item, modifier: Modifier = Modifier, onItemClick: (Item) -> Unit) {
    Surface(
        Modifier.fillMaxWidth().padding(12.dp),
        color = MaterialTheme.colors.primary.copy(alpha = 1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, MaterialTheme.colors.primary)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier.size(124.dp, 156.dp).padding(4.dp),
                backgroundColor = KafkaTheme.colors.surface,
                shape = RoundedCornerShape(6.dp),
                elevation = 0.dp
            ) {
                item.coverImage?.let { NetworkImage(url = it) }
            }
            SuggestedItemDescription(item)
        }
    }
}

@Composable
fun SuggestedItemDescription(item: Item) {
    Column(modifier = Modifier.padding(start = 16.dp)) {
        ProvideEmphasis(EmphasisAmbient.current.high) {
            Text(
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                text = item.title.toString(),
                maxLines = 2,
                style = MaterialTheme.typography.h4.decrementTextSize(4),
                color = KafkaTheme.colors.background
            )
        }

        ProvideEmphasis(EmphasisAmbient.current.medium) {
            Text(
                modifier = Modifier.padding(top = 4.dp),
                text = item.creator?.name.orEmpty(),
                maxLines = 1,
                style = MaterialTheme.typography.h6,
                color = KafkaTheme.colors.background.copy(alpha = 0.8f)
            )
        }

        ProvideEmphasis(EmphasisAmbient.current.medium) {
            Text(
                text = item.mediaType.orEmpty(),
                maxLines = 1,
                style = MaterialTheme.typography.body2,
                color = KafkaTheme.colors.background.copy(alpha = 0.6f)
            )
        }
    }
}

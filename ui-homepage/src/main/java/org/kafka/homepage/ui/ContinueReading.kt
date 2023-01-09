package org.kafka.homepage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemWithRecentItem
import org.kafka.common.ImmutableList
import org.kafka.common.shadowMaterial
import org.kafka.homepage.R
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.textPrimary
import ui.common.theme.theme.textSecondary

@Composable
internal fun ContinueReading(
    readingList: ImmutableList<ItemWithRecentItem>,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    val removeFromRecent by rememberSaveable { mutableStateOf(false) }

    if (readingList.items.isNotEmpty()) {
        Column(modifier = modifier) {
            Text(
                text = stringResource(id = R.string.continue_reading),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.textSecondary,
                modifier = Modifier.padding(horizontal = Dimens.Spacing20)
            )

            LazyRow(contentPadding = PaddingValues(end = 60.dp)) {
                items(readingList.items, key = { it.item.itemId }) {
                    ContinueReadingItem(it.item) { openItemDetail(it.item.itemId) }
                }
            }
        }
    } else {
        Spacer(modifier = Modifier.height(Dimens.Spacing12))
    }
}

@Composable
private fun ContinueReadingItem(continueReading: Item, onItemClicked: () -> Unit) {
    Column(modifier = Modifier
        .padding(Dimens.Spacing12)
        .clickable { onItemClicked() }
        .widthIn(100.dp, 286.dp)
    ) {
        Row(
            modifier = Modifier.padding(Dimens.Spacing12),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CoverImage(continueReading)
            Description(continueReading, Modifier.width(286.dp))
        }

        Spacer(modifier = Modifier.height(Dimens.Spacing12))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens.Spacing12)
                .padding(horizontal = 4.dp)
                .shadowMaterial(Dimens.Spacing12, clip = false)
                .clip(RoundedCornerShape(Dimens.Spacing02))
                .background(MaterialTheme.colorScheme.onPrimary)
        )
    }
}

@Composable
private fun CoverImage(item: Item) {
    Box(
        modifier = Modifier.shadowMaterial(
            elevation = Dimens.Spacing08,
            shape = RoundedCornerShape(Dimens.Spacing04)
        )
    ) {
        AsyncImage(
            model = item.run { coverImage ?: coverImageResource },
            contentDescription = stringResource(id = R.string.cd_cover_image),
            modifier = Modifier
                .size(64.dp, 76.dp)
                .background(MaterialTheme.colorScheme.surface),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun Description(continueReading: Item, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = continueReading.title.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(Dimens.Spacing02))
        Text(
            text = continueReading.mediaType.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(Dimens.Spacing08))

        Progress()
    }
}

@Composable
private fun Progress() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            progress = 0.2f,
            modifier = Modifier
                .height(Dimens.Spacing04)
                .width(116.dp)
                .clip(RoundedCornerShape(50))
        )
        Text(
            text = "20%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.textSecondary
        )
    }
}


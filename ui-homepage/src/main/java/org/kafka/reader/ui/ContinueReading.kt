package org.kafka.homepage.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.common.ImmutableList
import org.kafka.common.widgets.LoadImage
import org.kafka.ui_common_compose.shadowMaterial
import ui.common.theme.theme.textPrimary
import ui.common.theme.theme.textSecondary

@Composable
fun ContinueReading(
    readingList: ImmutableList<Item>,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    if (readingList.items.isNotEmpty()) {
        Column(modifier = modifier) {
            Text(
                text = "Continue Reading",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.textSecondary,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            LazyRow(contentPadding = PaddingValues(end = 60.dp)) {
                items(readingList.items.subList(0, 4), key = { it.itemId }) {
                    ContinueReadingItem(it) { openItemDetail(it.itemId) }
                }
            }
        }
    }
}

@Composable
private fun ContinueReadingItem(continueReading: Item, onItemClicked: () -> Unit) {
    Column(modifier = Modifier
        .padding(12.dp)
        .clickable { onItemClicked() }
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            CoverImage(continueReading.run { coverImage ?: coverImageResource })
            Description(continueReading)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Spacer(
            modifier = Modifier
                .width(328.dp)
                .height(12.dp)
                .padding(horizontal = 4.dp)
                .shadowMaterial(12.dp, clip = false)
                .clip(RoundedCornerShape(2.dp))
                .background(MaterialTheme.colorScheme.surface)
        )
    }
}

@Composable
private fun CoverImage(it: Any) {
    Card(shape = RoundedCornerShape(4.dp), elevation = CardDefaults.cardElevation(8.dp)) {
        LoadImage(data = it, modifier = Modifier.size(64.dp, 76.dp))
    }
}

@Composable
private fun Description(continueReading: Item) {
    Column {
        Text(
            text = continueReading.title.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.textPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = continueReading.mediaType.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(8.dp))

        Progress()
    }
}

@Composable
private fun Progress() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LinearProgressIndicator(
            progress = 0.2f,
            modifier = Modifier
                .height(4.dp)
                .width(156.dp)
                .clip(RoundedCornerShape(50))
        )
        Text(
            text = "20%",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.textSecondary
        )
    }
}

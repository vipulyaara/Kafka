package org.rekhta.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.common.widgets.LoadImage
import ui.common.theme.theme.textPrimary
import ui.common.theme.theme.textSecondary

@Composable
fun Item(item: Item, modifier: Modifier = Modifier, openItemDetail: (String) -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { openItemDetail(item.itemId) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CoverImage(item.run { coverImage ?: coverImageResource })
        Description(item)
    }
}

@Composable
private fun CoverImage(it: Any) {
    Card(shape = RoundedCornerShape(4.dp), elevation = CardDefaults.cardElevation(8.dp)) {
        LoadImage(data = it, modifier = Modifier.size(72.dp, 84.dp))
    }
}

@Composable
private fun Description(continueReading: Item) {
    Column {
        Text(
            text = continueReading.title.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.textPrimary
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = continueReading.creator?.name.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colorScheme.textSecondary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = continueReading.mediaType.orEmpty(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

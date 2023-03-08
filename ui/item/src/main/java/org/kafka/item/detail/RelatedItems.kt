package org.kafka.item.detail

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.ui.components.item.Item
import ui.common.theme.theme.Dimens

internal fun LazyListScope.relatedContent(
    relatedItems: List<Item>?,
    openItemDetail: (String) -> Unit
) {
    relatedItems?.takeIf { it.isNotEmpty() }?.let {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "More by author",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(Dimens.Spacing12)
            )
        }
        items(relatedItems, key = { it.itemId }) {
            Item(it, openItemDetail = openItemDetail)
        }
    }
}

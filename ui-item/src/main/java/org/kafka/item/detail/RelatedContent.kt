package org.kafka.item.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafka.data.entities.Item
import org.kafka.ui.components.item.Item
import ui.common.theme.theme.Dimens

@Composable
internal fun RelatedContent(items: List<Item>, openContentDetail: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "More by author",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(Dimens.Spacing12)
        )

        items.forEach {
            Item(it) { openContentDetail(it) }
        }
    }
}

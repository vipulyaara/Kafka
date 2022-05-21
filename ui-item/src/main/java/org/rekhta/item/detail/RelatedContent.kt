package org.rekhta.item.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.common.widgets.LoadImage
import org.rekhta.item.Item
import org.rekhta.navigation.LeafScreen
import org.rekhta.navigation.LocalNavigator
import org.rekhta.ui_common_compose.shadowMaterial

@Composable
internal fun RelatedContent(items: List<Item>) {
    val navigator = LocalNavigator.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "More by author",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(12.dp)
        )

        items.forEach {
            Item(it) { navigator.navigate(LeafScreen.ContentDetail.createRoute(it)) }
        }
    }
}

@Composable
private fun RelatedItem(item: Item) {
    LoadImage(
        data = item.coverImage,
        modifier = Modifier
            .size(136.dp, 176.dp)
            .padding(4.dp)
            .shadowMaterial(12.dp, RoundedCornerShape(4.dp))
    )
}

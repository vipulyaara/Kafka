package org.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens

@Composable
fun RowItem(item: Item, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        CoverImage(
            data = item.coverImage,
            size = Dimens.CoverSizeLarge,
            placeholder = placeholder(item.mediaType),
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadowMaterial(Dimens.Elevation04, RectangleShape)
                .background(MaterialTheme.colorScheme.surface)
        )

        Column(
            modifier = Modifier.padding(
                vertical = Dimens.Spacing08,
                horizontal = Dimens.Spacing04
            )
        ) {
            ItemTitleSmall(title = item.title.orEmpty())
            Spacer(modifier = Modifier.height(Dimens.Spacing02))
            Row(
                horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing08),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ItemMediaType(mediaType = item.mediaType, size = 12.dp)
                ItemCreatorSmall(creator = item.creator?.name)
            }
        }
    }
}

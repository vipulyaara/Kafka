package org.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.kafka.data.entities.Item
import org.kafka.common.widgets.LoadImage
import ui.common.theme.theme.Dimens

@Composable
private fun ItemLarge(item: Item, openItemDetail: (String) -> Unit) {
    Column(
        modifier = Modifier
            .width(164.dp)
            .clickable { openItemDetail(item.itemId) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {
            LoadImage(
                data = item.coverImage,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(4.dp)),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(Dimens.Spacing04)
            ) {
                ItemMediaType(mediaType = item.mediaType)
            }
        }

        Spacer(modifier = Modifier.size(Dimens.Spacing04))
        ItemTitleMedium(title = item.title, maxLines = 1)
        ItemCreator(
            creator = item.creator?.name,
            modifier = Modifier.padding(top = Dimens.Spacing02)
        )
    }
}

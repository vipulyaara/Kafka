package org.kafka.ui.components.item

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import com.kafka.data.entities.Item
import org.kafka.common.widgets.LoadImage
import org.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens

@Composable
fun LibraryItem(
    item: Item,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { openItemDetail(item.itemId) }
            .padding(Dimens.Spacing08)
    ) {
        LoadImage(
            data = item.coverImage,
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
            ItemTitleMedium(title = item.title.orEmpty())
            Spacer(modifier = Modifier.height(Dimens.Spacing02))
            ItemCreator(creator = item.creator?.name.orEmpty())
        }
    }
}

package org.kafka.library

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
import com.kafka.data.entities.FavoriteItem
import org.kafka.common.widgets.LoadImage
import org.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens

@Composable
internal fun LibraryItem(item: FavoriteItem, openItemDetail: (String) -> Unit) {
    Column(
        modifier = Modifier
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
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(Dimens.Spacing02))
            Text(
                text = item.creator,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

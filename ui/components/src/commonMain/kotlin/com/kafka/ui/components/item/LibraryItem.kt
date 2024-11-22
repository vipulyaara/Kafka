package com.kafka.ui.components.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafka.data.entities.Item
import ui.common.theme.theme.Dimens

@Composable
fun LibraryItem(item: Item, modifier: Modifier = Modifier, openItemDetail: (String) -> Unit) {
    LibraryItem(
        itemId = item.itemId,
        coverImage = item.coverImage,
        modifier = modifier,
        openItemDetail = openItemDetail
    )
}

@Composable
fun LibraryItem(
    itemId: String,
    coverImage: String?,
    modifier: Modifier = Modifier,
    openItemDetail: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { openItemDetail(itemId) }
            .padding(Dimens.Spacing04)
    ) {
        CoverImage(
            data = coverImage,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(0.66f),
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(Dimens.Radius08)
        )
    }
}

package com.kafka.ui.components.sheets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import ui.common.theme.theme.Dimens

@Composable
fun ActionBottomSheet(onDismiss: () -> Unit, actions: @Composable ColumnScope.() -> Unit) {
    StyledBottomSheet(onDismiss) {
        Column(verticalArrangement = Arrangement.spacedBy(Dimens.Spacing12)) {
            actions()
        }
    }
}

@Composable
fun ActionBottomSheetItem(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(Dimens.Gutter),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(Dimens.Spacing24))

        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(Dimens.Spacing24)
        )

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = Dimens.Gutter)
        )

        Spacer(modifier = Modifier.width(Dimens.Spacing24))
    }
}

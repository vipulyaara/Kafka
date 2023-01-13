package org.kafka.ui.components.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.kafka.common.Icons
import org.kafka.common.widgets.IconResource

@Composable
fun LayoutType(layoutType: LayoutType, changeViewType: (LayoutType) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        val icon = when (layoutType) {
            LayoutType.List -> Icons.Grid
            LayoutType.Grid -> Icons.List
        }

        IconButton(
            onClick = { changeViewType(layoutType.toggle()) },
            modifier = Modifier
        ) {
            IconResource(imageVector = icon, tint = MaterialTheme.colorScheme.primary)
        }
    }
}

enum class LayoutType {
    List,
    Grid;

    fun toggle() = if (this == List) Grid else List
}


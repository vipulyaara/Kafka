package com.kafka.ui.components.item

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconResource
import kafka.ui.components.generated.resources.Res
import kafka.ui.components.generated.resources.cd_change_layout
import org.jetbrains.compose.resources.stringResource

@Composable
fun LayoutType(layoutType: LayoutType, changeViewType: (LayoutType) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
        val icon = when (layoutType) {
            LayoutType.List -> Icons.Grid
            LayoutType.Grid -> Icons.List
        }

        IconButton(onClick = { changeViewType(layoutType.toggle()) }) {
            IconResource(
                imageVector = icon,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = stringResource(Res.string.cd_change_layout)
            )
        }
    }
}

enum class LayoutType {
    List,
    Grid;

    fun toggle() = if (this == List) Grid else List
}

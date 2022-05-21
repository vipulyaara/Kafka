/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package org.kafka.common.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.AppTheme

@Composable
fun <T> SelectableDropdownMenu(
    items: List<T>,
    selectedItem: T?,
    onItemSelect: (T) -> Unit,
    modifier: Modifier = Modifier,
    labelMapper: @Composable (T) -> String = { it.toString().replace("_", " ") },
    subtitles: List<String?>? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val dropIcon = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown

    Column(modifier = modifier) {
        TextButton(
            onClick = { expanded = !expanded },
            colors = ButtonDefaults.textButtonColors(contentColor = LocalContentColor.current),
            contentPadding = PaddingValues(
                vertical = AppTheme.specs.paddingSmall,
                horizontal = AppTheme.specs.paddingSmall
            ),
            modifier = Modifier.offset(x = 12.dp)
        ) {
            Text(text = if (selectedItem != null) labelMapper(selectedItem) else "    ")
            Spacer(Modifier.width(AppTheme.specs.paddingSmall))
            Icon(painter = rememberVectorPainter(dropIcon), contentDescription = "")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    onClick = {
                        expanded = !expanded
                        onItemSelect(item)
                    }
                ) {
                    Column {
                        Text(
                            text = labelMapper(item),
                            color = if (selectedItem == item) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onBackground
                        )

                        if (subtitles != null) {
                            val subtitle = subtitles[index]
                            if (subtitle != null)
                                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                                    Text(
                                        text = subtitle,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                        }
                    }
                }
            }
        }
    }
}

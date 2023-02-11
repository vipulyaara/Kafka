package org.kafka.item.files

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.Dimens

@Composable
internal fun ExtensionFilter(
    actionLabels: List<String>,
    selectedFilter: String?,
    onItemSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier.padding(end = Dimens.Spacing24),
        contentAlignment = Alignment.CenterEnd
    ) {
        OutlinedButton(
            onClick = { expanded = true },
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier
        ) {
            Text(
                text = selectedFilter ?: "all",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.animateContentSize()
            )
        }

        Box {
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .align(Alignment.Center)
                    .background(MaterialTheme.colorScheme.background)
                    .border(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                actionLabels.forEach { item ->
                    val color =
                        if (item == selectedFilter) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground

                    DropdownMenuItem(
                        text = { Text(text = item, color = color) },
                        onClick = {
                            expanded = false
                            if (item == "all") onItemSelected(null) else onItemSelected(item)
                        }
                    )
                }
            }
        }
    }
}

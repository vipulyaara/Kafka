@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.ui.components.sheets

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable

@Composable
fun StyledBottomSheet(onDismiss: () -> Unit, content: @Composable ColumnScope.() -> Unit) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = MaterialTheme.shapes.large,
        containerColor = MaterialTheme.colorScheme.background,
        dragHandle = { BottomSheetDefaults.DragHandle(color = MaterialTheme.colorScheme.tertiary) },
        scrimColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.32f),
        content = content
    )
}

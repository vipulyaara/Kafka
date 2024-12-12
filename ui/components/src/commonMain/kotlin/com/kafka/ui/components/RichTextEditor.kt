@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mohamedrejeb.richeditor.model.RichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor

@Composable
fun RichTextEditor(textState: RichTextState, modifier: Modifier = Modifier) {
    OutlinedRichTextEditor(
        state = textState,
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyMedium
    )
}

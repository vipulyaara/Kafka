package org.kafka.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.Dimens

@Composable
fun MessageBox(text: String, modifier: Modifier = Modifier) {
    Surface(modifier = modifier, border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary)) {
        Text(
            text = text,
            modifier = Modifier.padding(Dimens.Spacing24),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

package com.kafka.content.compose.widgets

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import com.kafka.ui.theme.KafkaColors

@Composable
fun Snackbar(text: String) {
    Box() {
        Card {
            Text(text = text, style = MaterialTheme.typography.body2, color = KafkaColors.textPrimary)
        }
    }
}

package com.kafka.ui_common.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kafka.ui_common.theme.KafkaTheme

@Composable
fun FullScreenLoader() {
    Box(modifier = Modifier.fillMaxSize(), alignment = Alignment.Center) {
        CircularProgressIndicator(
            modifier = Modifier.size(40.dp),
            color = KafkaTheme.colors.secondary
        )
    }
}

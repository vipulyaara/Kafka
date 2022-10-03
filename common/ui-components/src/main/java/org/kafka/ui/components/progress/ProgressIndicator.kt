package org.kafka.ui.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.kafka.base.debug
import org.kafka.common.widgets.LoadImage
import org.kafka.ui.components.R

@Composable
fun FullScreenProgressBar(modifier: Modifier = Modifier, show: Boolean = true) {
    if (show) {
        debug { "show loading $show" }
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(48.dp)
                .zIndex(2f)
        ) {
            LoadImage(
                modifier = Modifier
                    .size(96.dp)
                    .align(Alignment.Center),
                data = R.drawable.ic_kafka_logo,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

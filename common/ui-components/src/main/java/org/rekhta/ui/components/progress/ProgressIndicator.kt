package org.rekhta.ui.components.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.kafka.common.extensions.rememberMutableState
import org.kafka.common.widgets.LoadImage
import org.rekhta.base.debug
import org.rekhta.ui.components.R
import ui.common.theme.theme.iconPrimary

@Composable
fun FullScreenProgressBar(modifier: Modifier = Modifier, show: Boolean = true) {
    var isVisible by rememberMutableState(init = { false })
    LaunchedEffect(show) { isVisible = show }

    if (isVisible) {
        debug { "show loading $isVisible" }
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(48.dp)
                .zIndex(2f)
        ) {
            LoadImage(
                modifier = Modifier
                    .size(76.dp)
                    .align(Alignment.Center),
                data = R.drawable.ic_rekhta_r,
                tint = MaterialTheme.colorScheme.iconPrimary
            )
        }
    }
}

package org.kafka.common.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@ExperimentalMaterial3Api
@Composable
fun DefaultScaffold(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.background,
    topBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        containerColor = color,
        contentColor = contentColor,
        snackbarHost = snackbarHost
    ) {
        Box(modifier = Modifier.padding(it)) {
            content()
        }
    }
}

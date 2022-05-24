package org.kafka.homepage.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import org.kafka.common.Icons
import org.kafka.common.extensions.elevation
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource
import org.kafka.ui.components.material.TopBar

@Composable
fun HomepageTopBar(lazyListState: LazyListState = rememberLazyListState()) {
    TopBar(
        actions = {
            IconButton(onClick = { }) {
                IconResource(imageVector = Icons.Sun, tint = MaterialTheme.colorScheme.primary)
            }
        },
        elevation = derivedStateOf { lazyListState.elevation() }.value
    )
}

package org.rekhta.homepage.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import org.kafka.common.Icons
import org.kafka.common.widgets.IconButton
import org.kafka.common.widgets.IconResource

@Composable
fun HomepageTopBar() {
    SmallTopAppBar(
        title = {

        },
        actions = {
            IconButton(onClick = { }) {
                IconResource(imageVector = Icons.Sun, tint = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

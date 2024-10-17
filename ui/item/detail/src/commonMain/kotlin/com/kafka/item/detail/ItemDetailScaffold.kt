package com.kafka.item.detail

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kafka.ui.components.scaffoldPadding

@Composable
fun ItemDetailScaffold(
    supportingPaneEnabled: Boolean = true,
    mainPane: LazyGridScope.() -> Unit,
    supportingPane: LazyGridScope.() -> Unit
) {
    Row {
        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxHeight()
                .weight(0.5f),
            contentPadding = scaffoldPadding(),
            columns = GridCells.Fixed(1)
        ) {
            mainPane()

            if (!supportingPaneEnabled) {
                supportingPane()
            }
        }

        if (supportingPaneEnabled) {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f),
                contentPadding = scaffoldPadding(),
                columns = GridCells.Fixed(1)
            ) {
                supportingPane()
            }
        }
    }
}

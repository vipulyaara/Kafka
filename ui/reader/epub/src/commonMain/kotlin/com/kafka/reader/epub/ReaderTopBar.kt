@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.kafka.common.extensions.getContext
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconResource
import com.kafka.ui.components.material.TopBar

@Composable
fun ReaderTopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    viewModel: EpubReaderViewModel,
    containerColor: Color
) {
    val context = getContext()

    TopBar(
        containerColor = containerColor.copy(alpha = 0.7f),
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            ActionIcon(icon = Icons.Back, contentDescription = "Back", onClick = viewModel::goBack)
        },
        actions = {
            ActionIcon(
                icon = Icons.Share,
                contentDescription = "Share"
            ) { viewModel.shareItemText(context) }

            ActionIcon(Icons.List, "Table of contents") {
                viewModel.showTocSheet.value = true
            }
        },
    )
}

@Composable
private fun ActionIcon(icon: ImageVector, contentDescription: String? = null, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        IconResource(
            imageVector = icon,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = contentDescription
        )
    }
}

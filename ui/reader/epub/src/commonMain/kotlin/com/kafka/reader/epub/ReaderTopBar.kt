@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.reader.epub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.common.widgets.IconResource
import com.kafka.ui.components.material.TopBar
import ui.common.theme.theme.Dimens

@Composable
internal fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    onShareClicked: () -> Unit,
    onBackPressed: () -> Unit,
) {
    TopBar(
        containerColor = Color.Transparent,
        navigationIcon = {
            IconButton(
                onClick = { onBackPressed() },
                modifier = Modifier
                    .padding(Dimens.Spacing08)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                IconResource(
                    imageVector = Icons.Back,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onShareClicked() },
                modifier = Modifier
                    .padding(Dimens.Spacing08)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                IconResource(
                    imageVector = Icons.Share,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = "Back"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

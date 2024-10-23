@file:OptIn(ExperimentalMaterial3Api::class)

package com.kafka.item.detail

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.kafka.common.extensions.AnimatedVisibilityFade
import com.kafka.common.image.Icons
import com.kafka.common.widgets.IconButton
import com.kafka.common.widgets.IconResource
import com.kafka.ui.components.material.TopBar
import kafka.ui.item.detail.generated.resources.Res
import kafka.ui.item.detail.generated.resources.cd_back_button
import org.jetbrains.compose.resources.stringResource
import ui.common.theme.theme.Dimens

@Composable
internal fun TopBar(
    onShareClicked: () -> Unit,
    onBackPressed: () -> Unit,
    lazyGridState: LazyGridState,
    isShareVisible: Boolean = true
) {
    val isRaised by remember { derivedStateOf { lazyGridState.firstVisibleItemIndex > 2 } }

    val containerColor by animateColorAsState(
        targetValue = if (isRaised) MaterialTheme.colorScheme.primary else Color.Transparent,
        label = "container_color"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isRaised) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
        label = "content_color"
    )

    TopBar(
        containerColor = Color.Transparent,
        navigationIcon = {
            BackIcon(
                onBackPressed = onBackPressed,
                containerColor = containerColor,
                contentColor = contentColor
            )
        },
        actions = {
            if (isShareVisible) {
                ShareIcon(
                    isRaised = isRaised,
                    onClick = onShareClicked
                )
            }
        }
    )
}

@Composable
private fun ShareIcon(
    isRaised: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibilityFade(!isRaised) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.padding(Dimens.Spacing08)
        ) {
            IconResource(
                imageVector = Icons.Share,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BackIcon(
    onBackPressed: () -> Unit,
    containerColor: Color,
    contentColor: Color
) {
    IconButton(
        onClick = { onBackPressed() },
        modifier = Modifier
            .padding(Dimens.Spacing08)
            .clip(CircleShape)
            .background(containerColor)
    ) {
        IconResource(
            imageVector = Icons.Back,
            tint = contentColor,
            contentDescription = stringResource(Res.string.cd_back_button)
        )
    }
}


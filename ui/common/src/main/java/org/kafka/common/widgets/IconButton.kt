package org.kafka.common.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val RippleRadius = 24.dp
private val IconButtonSizeModifier = Modifier.size(48.dp)

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    rippleColor: Color = Color.Unspecified,
    rippleRadius: Dp = RippleRadius,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    tint: Color = MaterialTheme.colorScheme.onBackground,
    imageVector: ImageVector
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        rippleColor = rippleColor,
        rippleRadius = rippleRadius,
        interactionSource = interactionSource
    ) {
        IconResource(imageVector = imageVector, contentDescription = null, tint = tint)
    }
}

@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    rippleColor: Color = Color.Unspecified,
    rippleRadius: Dp = RippleRadius,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    color = rippleColor,
                    radius = rippleRadius
                )
            )
            .then(IconButtonSizeModifier),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

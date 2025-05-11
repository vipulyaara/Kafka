package com.kafka.common

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.semantics.Role

fun Modifier.simpleClickable(
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    indication: Indication? = null,
    onClick: () -> Unit,
) = composed {
    clickable(
        enabled = enabled,
        onClick = onClick,
        role = Role.Button,
        indication = indication,
        interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    )
}

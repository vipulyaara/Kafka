package com.kafka.kms.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kafka.common.image.Icons
import com.kafka.data.prefs.Theme

@Composable
fun ThemeSwitch(
    theme: Theme,
    onThemeChange: (Theme) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ThemeOption(
            theme = Theme.LIGHT,
            selected = theme == Theme.LIGHT,
            onClick = { onThemeChange(Theme.LIGHT) },
            icon = Icons.Sun
        )
        ThemeOption(
            theme = Theme.DARK,
            selected = theme == Theme.DARK,
            onClick = { onThemeChange(Theme.DARK) },
            icon = Icons.Moon
        )
        ThemeOption(
            theme = Theme.SYSTEM,
            selected = theme == Theme.SYSTEM,
            onClick = { onThemeChange(Theme.SYSTEM) },
            icon = Icons.System
        )
    }
}

@Composable
private fun ThemeOption(
    theme: Theme,
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .border(
                width = 1.5.dp,
                color = if (selected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.outline,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        color = if (selected) 
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else 
            MaterialTheme.colorScheme.surface
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "Switch to ${theme.name.lowercase()} theme",
            modifier = Modifier.padding(8.dp),
            tint = if (selected)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 
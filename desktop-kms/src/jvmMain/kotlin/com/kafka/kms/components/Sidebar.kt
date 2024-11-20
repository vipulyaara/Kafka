package com.kafka.kms.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import compose.icons.TablerIcons
import compose.icons.tablericons.BoltOff
import compose.icons.tablericons.Book
import compose.icons.tablericons.Feather
import compose.icons.tablericons.Settings
import compose.icons.tablericons.Upload

@Composable
fun Sidebar(
    selectedRoute: String,
    onRouteSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(220.dp)
            .fillMaxHeight()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Logo
        Text(
            text = "KMS",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Search
        TextField(
            value = "",
            onValueChange = { },
            placeholder = "Search",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Books Section
        NavItem(
            icon = TablerIcons.Book,
            label = "Books",
            selected = selectedRoute == "books",
            onClick = { onRouteSelected("books") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Libraries Section
        Text(
            text = "LIBRARIES",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )

        NavItem(
            icon = TablerIcons.Feather,
            label = "Gutenberg",
            selected = selectedRoute == "gutenberg",
            onClick = { onRouteSelected("gutenberg") }
        )

        NavItem(
            icon = TablerIcons.BoltOff,
            label = "Standard Ebooks",
            selected = selectedRoute == "standard-ebooks",
            onClick = { onRouteSelected("standard-ebooks") }
        )

        NavItem(
            icon = TablerIcons.Upload,
            label = "Upload",
            selected = selectedRoute == "upload",
            onClick = { onRouteSelected("upload") }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Settings at bottom
        NavItem(
            icon = TablerIcons.Settings,
            label = "Settings",
            selected = selectedRoute == "settings",
            onClick = { onRouteSelected("settings") }
        )
    }
}

@Composable
private fun NavItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (selected) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        Color.Transparent
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(6.dp))
            .background(background)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = if (selected) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = if (selected) {
                MaterialTheme.colorScheme.onSecondaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
} 
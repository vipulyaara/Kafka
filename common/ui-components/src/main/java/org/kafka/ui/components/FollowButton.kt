package org.kafka.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FollowButton(isLiked: Boolean, onClicked: () -> Unit) {
    val favoriteColor by animateColorAsState(targetValue = if (isLiked) colorScheme.surface else colorScheme.primary)
    val favoriteText = if (isLiked) "Followed" else "Follow"
    val textColor = if (isLiked) colorScheme.primary else colorScheme.onPrimary

    Card(
        modifier = Modifier
            .clickable { onClicked() },
        backgroundColor = favoriteColor,
        elevation = 1.dp,
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(2.dp, colorScheme.primary)
    ) {
        Text(
            text = favoriteText,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.titleSmall,
            color = textColor
        )
    }
}

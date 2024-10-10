package com.kafka.ui.components.item

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ui.common.theme.theme.Dimens

@Composable
fun SubjectItem(text: String, modifier: Modifier = Modifier, onClicked: (() -> Unit)? = null) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .then(onClicked?.let { Modifier.clickable(onClick = it) } ?: Modifier)
                .padding(
                    horizontal = Dimens.Spacing12,
                    vertical = Dimens.Spacing08
                )
        )
    }
}

@Composable
fun GenreItem(text: String, modifier: Modifier = Modifier, onClicked: () -> Unit = {}) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.background,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier
                .clickable { onClicked() }
                .padding(
                    horizontal = Dimens.Spacing12,
                    vertical = Dimens.Spacing08
                )
        )
    }
}

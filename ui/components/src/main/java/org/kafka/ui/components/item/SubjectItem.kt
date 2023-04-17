package org.kafka.ui.components.item

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.common.theme.theme.Dimens

@Composable
fun SubjectItem(text: String, modifier: Modifier = Modifier, onClicked: () -> Unit) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .clickable { onClicked() }
                .padding(
                    horizontal = Dimens.Spacing12,
                    vertical = Dimens.Spacing08
                )
        )
    }
}

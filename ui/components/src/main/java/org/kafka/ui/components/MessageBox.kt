package org.kafka.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.kafka.common.widgets.IconResource
import ui.common.theme.theme.Dimens

@Composable
fun MessageBox(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector? = null,
    trailingIcon: ImageVector? = null,
    onClick: (() -> Unit)? = null,
    onIconClick: (() -> Unit)? = onClick
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(Dimens.RadiusMedium),
        border = BorderStroke(
            width = 1.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        ),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .padding(Dimens.Spacing16),
            horizontalArrangement = Arrangement.spacedBy(Dimens.Spacing12),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                IconResource(
                    imageVector = leadingIcon,
                    modifier = Modifier.size(Dimens.Spacing20),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )

            if (trailingIcon != null) {
                IconResource(
                    imageVector = trailingIcon,
                    modifier = Modifier.size(Dimens.Spacing20),
                    tint = MaterialTheme.colorScheme.primary,
                    onClick = onIconClick ?: {}
                )
            }
        }
    }
}

package org.rekhta.ui.components.material

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.rekhta.ui_common_compose.shadowMaterial
import ui.common.theme.theme.textPrimary

@Composable
fun FloatingButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.background,
    onClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .shadowMaterial(12.dp, RoundedCornerShape(2.dp))
            .background(containerColor)
            .clickable { onClicked() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.textPrimary,
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 14.dp)
                .align(Alignment.Center)
        )
    }
}

package org.kafka.ui.components.material

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.kafka.common.extensions.alignCenter
import org.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens

@Composable
fun FloatingButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onClickLabel: String? = null,
    onClicked: () -> Unit,
) {
    Surface(
        modifier = modifier
            .shadowMaterial(Dimens.Spacing12, RoundedCornerShape(Dimens.Spacing04))
            .background(containerColor)
            .clickable(onClickLabel = onClickLabel) { onClicked() },
        shape = RoundedCornerShape(Dimens.Spacing04),
        color = containerColor,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.alignCenter(),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 14.dp)
        )
    }
}

@Composable
fun PrimaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(Dimens.Spacing08),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(vertical = Dimens.Spacing08),
            text = text,
            style = MaterialTheme.typography.titleSmall.alignCenter()
        )
    }
}

@Preview
@Composable
fun FloatingButtonPreview() {
    FloatingButton(text = "Floating Button", onClicked = {})
}
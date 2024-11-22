package com.kafka.ui.components.material

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.kafka.common.extensions.alignCenter
import com.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens
import ui.common.theme.theme.inverseOnSurfaceDeep
import ui.common.theme.theme.inverseSurfaceDeep

@Composable
fun FloatingButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.inverseSurfaceDeep,
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
            style = MaterialTheme.typography.labelLarge.alignCenter(),
            color = MaterialTheme.colorScheme.inverseOnSurfaceDeep,
            modifier = Modifier.padding(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing12)
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

@Composable
fun SecondaryButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    OutlinedButton(
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
            style = MaterialTheme.typography.titleSmall.alignCenter(),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun TextButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(Dimens.Spacing08),
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleSmall.alignCenter(),
            color = color
        )
    }
}

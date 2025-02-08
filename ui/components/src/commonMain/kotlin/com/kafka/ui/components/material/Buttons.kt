package com.kafka.ui.components.material

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.kafka.common.extensions.alignCenter
import com.kafka.common.widgets.shadowMaterial
import ui.common.theme.theme.Dimens

@Composable
fun FloatingButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    contentPadding: PaddingValues = PaddingValues(horizontal = Dimens.Spacing24, vertical = Dimens.Spacing12),
    elevation: Dp = Dimens.Spacing12,
    shape: Shape = RoundedCornerShape(Dimens.Spacing02),
    textStyle: TextStyle = MaterialTheme.typography.labelLarge,
    onClickLabel: String? = null,
    onClick: () -> Unit,
) {
    val alpha = if (enabled) 1f else 0.38f
    val buttonColor = if (enabled) containerColor else containerColor.copy(alpha = 0.12f)
    val textColor = contentColor
    
    Surface(
        modifier = modifier
            .alpha(alpha)
            .shadowMaterial(if (enabled) elevation else Dimens.Spacing00, shape)
            .background(buttonColor)
            .clickable(
                enabled = enabled,
                onClickLabel = onClickLabel
            ) { onClick() },
        shape = shape,
        color = buttonColor,
    ) {
        Text(
            text = text,
            style = textStyle.alignCenter(),
            color = textColor,
            modifier = Modifier.padding(contentPadding)
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
        colors = ButtonDefaults.buttonColors(
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
        ),
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
        colors = ButtonDefaults.outlinedButtonColors(
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
        ),
        onClick = onClick
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
                .padding(vertical = Dimens.Spacing08),
            text = text,
            style = MaterialTheme.typography.titleSmall.alignCenter(),
            color = if (enabled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
}

@Composable
fun TextButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = MaterialTheme.colorScheme.primary,
    contentPadding: PaddingValues = ButtonDefaults.TextButtonContentPadding,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(Dimens.Spacing08),
        contentPadding = contentPadding,
        colors = ButtonDefaults.textButtonColors(
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        ),
        onClick = onClick
    ) {
        Text(
            text = text,
            modifier = Modifier.align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleSmall.alignCenter(),
            color = if (enabled) color else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
        )
    }
}

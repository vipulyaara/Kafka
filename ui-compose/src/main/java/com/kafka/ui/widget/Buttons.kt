package com.kafka.ui.widget

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.foundation.Border
import androidx.ui.foundation.Text
import androidx.ui.foundation.shape.RectangleShape
import androidx.ui.graphics.Color
import androidx.ui.graphics.Shape
import androidx.ui.layout.EdgeInsets
import androidx.ui.layout.InnerPadding
import androidx.ui.layout.LayoutPadding
import androidx.ui.material.Button
import androidx.ui.material.MaterialTheme
import androidx.ui.material.contentColorFor
import androidx.ui.unit.Dp
import androidx.ui.unit.dp
import com.kafka.ui.alignCenter

@Composable
fun ButtonRegular(
    modifier: Modifier = Modifier.None,
    onClick: (() -> Unit) = {},
    text: String,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    shape: Shape = RectangleShape,
    elevation: Dp = 12.dp
) =
    Button(
        modifier = modifier,
        backgroundColor = backgroundColor,
        onClick = onClick,
        shape = shape,
        elevation = elevation,
        contentColor = contentColor,
        padding = regularButtonPadding
    ) {
        Text(text = text, style = MaterialTheme.typography.button.alignCenter())
    }

@Composable
fun ButtonSmall(
    modifier: Modifier = Modifier.None,
    onClick: (() -> Unit) = {},
    text: String,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = contentColorFor(backgroundColor),
    shape: Shape = RectangleShape,
    elevation: Dp = 2.dp,
    border: Border? = Border(1.dp, MaterialTheme.colors.background),
    paddings: InnerPadding = smallButtonPadding
) =
    Button(
        modifier = modifier,
        backgroundColor = backgroundColor,
        onClick = onClick,
        shape = shape,
        contentColor = contentColor,
        border = border,
        elevation = elevation,
        padding = paddings
    ) {
        Text(text = text, style = MaterialTheme.typography.button.alignCenter(), maxLines = 1)
    }

val regularButtonPadding =
    InnerPadding(start = 24.dp, end = 24.dp, top = 16.dp, bottom = 16.dp)

val smallButtonPadding =
    InnerPadding(top = 10.dp, bottom = 10.dp, start = 24.dp, end = 24.dp)

val tinyButtonPadding = LayoutPadding(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 4.dp)

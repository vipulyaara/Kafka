package com.kafka.ui

import androidx.compose.Composable
import androidx.ui.core.Modifier
import androidx.ui.graphics.Color
import androidx.ui.material.ripple.ripple
import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.TextUnit

fun TextStyle.singleLine() = merge(TextStyle())

fun TextStyle.alignCenter() = merge(TextStyle(textAlign = TextAlign.Center))

fun TextStyle.justify() = merge(TextStyle(textAlign = TextAlign.Justify))

fun TextStyle.lineHeight(value: Double) =
    merge(TextStyle(lineHeight = TextUnit.Companion.Em(value)))

@Composable
fun TextStyle.alpha(alpha: Float) = merge(TextStyle(color = color.copy(alpha = alpha)))

@Composable
fun Color.alpha(alpha: Float) = copy(alpha = alpha)

@Composable
fun Clickable(
    onClick: () -> Unit,
    modifier: Modifier  = Modifier.ripple(),
    children: @Composable() () -> Unit
) {
    androidx.ui.foundation.Clickable(onClick = onClick, modifier = modifier, children = children)
}

fun TextStyle.decrementTextSize(step: Int = 2) =
    copy(fontSize = fontSize.minus(TextUnit.Companion.Sp(step)))

fun TextStyle.incrementTextSize(step: Int = 2) =
    copy(fontSize = fontSize.plus(TextUnit.Companion.Sp(step)))

const val bulletSymbolWithSpace = "  •  "
const val bulletSymbol = "•"

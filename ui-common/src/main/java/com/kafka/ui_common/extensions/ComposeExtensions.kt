package com.kafka.ui_common.extensions

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit

fun TextStyle.alignCenter() = merge(TextStyle(textAlign = TextAlign.Center))

fun TextStyle.decrementTextSize(step: Int = 2) =
    copy(fontSize = fontSize.minus(TextUnit.Companion.Sp(step)))

fun TextStyle.incrementTextSize(step: Int = 2) =
    copy(fontSize = fontSize.plus(TextUnit.Companion.Sp(step)))

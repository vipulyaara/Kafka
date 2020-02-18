package com.kafka.ui

import androidx.ui.text.TextStyle
import androidx.ui.text.style.TextAlign
import androidx.ui.unit.TextUnit

fun TextStyle.alignCenter() = merge(TextStyle(textAlign = TextAlign.Center))

fun TextStyle.justify() = merge(TextStyle(textAlign = TextAlign.Justify))

fun TextStyle.lineHeight(value: Double) =
    merge(TextStyle(lineHeight = TextUnit.Companion.Em(value)))

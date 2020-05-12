package com.kafka.ui

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import androidx.annotation.StyleRes
import androidx.compose.Composable
import androidx.core.content.res.getResourceIdOrThrow
import androidx.core.content.res.use
import androidx.core.graphics.ColorUtils
import androidx.ui.core.DensityAmbient
import androidx.ui.geometry.Offset
import androidx.ui.graphics.Color
import androidx.ui.graphics.Shadow
import androidx.ui.material.MaterialTheme
import androidx.ui.material.Typography
import androidx.ui.material.darkColorPalette
import androidx.ui.material.lightColorPalette
import androidx.ui.text.TextStyle
import androidx.ui.text.font.*
import androidx.ui.unit.TextUnit
import androidx.ui.unit.em
import androidx.ui.unit.px

@Composable
fun colors() = MaterialTheme.colors

fun backgroundLight() = Color(0xfff8f8f8)

@Composable
fun typography() = MaterialTheme.typography

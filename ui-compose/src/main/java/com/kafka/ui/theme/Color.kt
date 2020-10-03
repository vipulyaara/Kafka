package com.kafka.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import kotlin.math.max
import kotlin.math.min

val primary = Color(0xff3D64FF)
val secondary = Color(0xffAF945C)
val background = Color(0xffffffff)
val surface = Color(0xffDFE9FF)
val textPrimary = Color(0xff313131)
val textSecondary = Color(0xff7B8994)
val iconPrimary = Color(0xff000000)

fun Color.constrastAgainst(background: Color): Float {
    val fg = if (alpha < 1f) compositeOver(background) else this

    val fgLuminance = fg.luminance() + 0.05f
    val bgLuminance = background.luminance() + 0.05f

    return max(fgLuminance, bgLuminance) / min(fgLuminance, bgLuminance)
}

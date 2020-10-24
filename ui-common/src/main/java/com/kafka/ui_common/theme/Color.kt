package com.kafka.ui_common.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import kotlin.math.max
import kotlin.math.min

val primary = Color(0xffAF945C)
val secondary = Color(0xffAF945C)
val background = Color(0xffF6F9FE)
val surface = Color(0xffFFFFFF)
val textPrimary = Color(0xff232323)
val textSecondary = Color(0xff7B8994)
val iconPrimary = Color(0xff000000)
val iconSecondary = Color(0xff7B8994)

val primaryDark = Color(0xfffca311)
val secondaryDark = Color(0xfffca311)
val backgroundDark = Color(0xff000000)
val surfaceDark = Color(0xff172027)
val textPrimaryDark = Color(0xffffffff)
val textSecondaryDark = Color(0xff888888)
val iconPrimaryDark = Color(0xffffffff)
val iconSecondaryDark = Color(0xffffffff)

fun Color.constrastAgainst(background: Color): Float {
    val fg = if (alpha < 1f) compositeOver(background) else this

    val fgLuminance = fg.luminance() + 0.05f
    val bgLuminance = background.luminance() + 0.05f

    return max(fgLuminance, bgLuminance) / min(fgLuminance, bgLuminance)
}

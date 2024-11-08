package com.kafka.common

import androidx.compose.ui.graphics.Color

fun String.toColor(): Color {
    // Remove # if present
    val hex = if (startsWith("#")) substring(1) else this

    // Handle different hex formats
    return when (hex.length) {
        // #RGB format
        3 -> Color(
            red = hex[0].toString().repeat(2).toInt(16) / 255f,
            green = hex[1].toString().repeat(2).toInt(16) / 255f,
            blue = hex[2].toString().repeat(2).toInt(16) / 255f,
            alpha = 1f
        )
        // #RGBA format
        4 -> Color(
            red = hex[0].toString().repeat(2).toInt(16) / 255f,
            green = hex[1].toString().repeat(2).toInt(16) / 255f,
            blue = hex[2].toString().repeat(2).toInt(16) / 255f,
            alpha = hex[3].toString().repeat(2).toInt(16) / 255f
        )
        // #RRGGBB format
        6 -> Color(
            red = hex.substring(0, 2).toInt(16) / 255f,
            green = hex.substring(2, 4).toInt(16) / 255f,
            blue = hex.substring(4, 6).toInt(16) / 255f,
            alpha = 1f
        )
        // #RRGGBBAA format
        8 -> Color(
            red = hex.substring(0, 2).toInt(16) / 255f,
            green = hex.substring(2, 4).toInt(16) / 255f,
            blue = hex.substring(4, 6).toInt(16) / 255f,
            alpha = hex.substring(6, 8).toInt(16) / 255f
        )
        else -> throw IllegalArgumentException("Invalid hex color string: $this")
    }
}

fun Color.toHexString(includeAlpha: Boolean = false): String {
    val r = (red * 255).toInt().toString(16).padStart(2, '0')
    val g = (green * 255).toInt().toString(16).padStart(2, '0')
    val b = (blue * 255).toInt().toString(16).padStart(2, '0')
    
    return if (includeAlpha) {
        val a = (alpha * 255).toInt().toString(16).padStart(2, '0')
        "#$r$g$b$a"
    } else {
        "#$r$g$b"
    }
}

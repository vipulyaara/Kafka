package com.kafka.user.ui

import android.graphics.Color
import android.graphics.ColorFilter

import androidx.annotation.ColorInt

object ColorUtils {
    @ColorInt
    fun darkenColor(@ColorInt color: Int, factor: Float): Int {
        val hsv = FloatArray(3)
        Color.RGBToHSV(Color.red(color), Color.green(color), Color.blue(color), hsv)
        hsv[2] = hsv[2] * factor
        return Color.HSVToColor(hsv)
    }

    fun getColorFilter(@ColorInt color: Int, alpha: Float): ColorFilter? {
        return ColorFilterCache.getColorFilterCache(color).getFilterForLevel(alpha)
    }
}

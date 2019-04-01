package com.kafka.user.ui

import android.graphics.Color
import android.graphics.drawable.GradientDrawable

/**
 * @author Vipul Kumar; dated 29/03/19.
 */
object GradientGenerator {
    private val gradientColors =
        arrayListOf(
            Gradient("2E3192", "1BFFFF"),
            Gradient("D4145A", "FBB03B"),
            Gradient("009245", "FCEE21"),
            Gradient("662D8C", "ED1E79"),
            Gradient("ED1C24", "FCEE21"),
            Gradient("00A8C5", "FFFF7E"),
            Gradient("D74177", "FFE98A"),
            Gradient("FB872B", "D9E021"),
            Gradient("009E00", "FFFF96"),
            Gradient("B066FE", "63E2FF"),
            Gradient("00537E", "3AA17E"),
            Gradient("D585FF", "00FFEE"),
            Gradient("4F00BC", "29ABE2")
        )

    private fun generateRandomGradient() = gradientColors.random()

    fun generateRandomBackground(): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            generateRandomGradient().getColorArray()
        ).also { it.cornerRadius = 0f }
    }

    class Gradient(
        private val startColor: String,
        private val endColor: String,
        val centerColor: String? = null,
        val angle: Int = 45
    ) {
        fun getColorArray(): IntArray {
            return intArrayOf(
                Color.parseColor(endColor),
                Color.parseColor(startColor)
            )
        }
    }
}

package com.kafka.user.ui.palette

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap

import com.bumptech.glide.Glide

object PaletteUtil {
    private var sPaletteTranscoderRegistered = false

    fun registerGlidePaletteTranscoder(context: Context) {
        if (!sPaletteTranscoderRegistered) {
            val resources = context.resources
            Glide.get(context).registry.register(
                Bitmap::class.java,
                PaletteBitmapContainer::class.java,
                PaletteBitmapTranscoder(context)
            )
            sPaletteTranscoderRegistered = true
        }
    }
}

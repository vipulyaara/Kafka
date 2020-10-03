package com.kafka.ui_common.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.palette.graphics.Palette
import coil.ImageLoader
import coil.bitmap.BitmapPool
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import coil.transform.Transformation
import kotlinx.coroutines.coroutineScope

class PalettePlaceholderTransformation(private val block: (Int) -> Unit) : Transformation {
    override fun key() = "paletteTransformer"

    override suspend fun transform(pool: BitmapPool, input: Bitmap, size: Size): Bitmap {
        val p = Palette.from(input).generate()
        block(p.getDarkVibrantColor(0))
        val swatch = p.vibrantSwatch
        return input
    }
}

suspend fun loadColorFromUrl(context: Context, url: String, block: (Int) -> Unit) {
    coroutineScope {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(url)
            .allowHardware(false) // Disable hardware bitmaps.
            .build()

        val result = (loader.execute(request) as SuccessResult).drawable
        val bitmap = (result as BitmapDrawable).bitmap
        val p = Palette.from(bitmap).generate()
        block(p.getDarkVibrantColor(0))
    }
}

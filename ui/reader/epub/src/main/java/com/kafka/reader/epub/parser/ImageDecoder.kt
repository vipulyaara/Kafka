package com.kafka.reader.epub.parser

import android.graphics.BitmapFactory

object ImageDecoder {
    fun getAspectRatio(imageData: ByteArray): Float {
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeByteArray(imageData, 0, imageData.size, options)
        return options.outHeight.toFloat() / options.outWidth.toFloat()
    }
}

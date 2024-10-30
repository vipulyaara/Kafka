@file:OptIn(ExperimentalForeignApi::class)

package com.kafka.reader.epub.parser

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.useContents
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.dataWithBytes
import platform.UIKit.UIImage

actual object ImageDecoder {
    actual fun getAspectRatio(imageData: ByteArray): Float {
        val nsData = imageData.usePinned { pinned ->
            NSData.dataWithBytes(pinned.addressOf(0), imageData.size.toULong())
        }
        val image = UIImage.imageWithData(nsData)
        val imageSize = image?.size ?: return 1f
        return (imageSize.useContents { height } / imageSize.useContents { width }).toFloat()
    }
}

package com.kafka.reader.epub.parser

import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

actual object ImageDecoder {
    actual fun getAspectRatio(imageData: ByteArray): Float {
        val image = ImageIO.read(ByteArrayInputStream(imageData))
        return image.height.toFloat() / image.width.toFloat()
    }
}

package com.kafka.reader.epub.parser

import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

actual object ImageDecoder {
    actual fun getAspectRatio(imageData: ByteArray): Float {
        val inputStream = ByteArrayInputStream(imageData)
        val bufferedImage = ImageIO.read(inputStream)
        return bufferedImage.height.toFloat() / bufferedImage.width.toFloat()
    }
}

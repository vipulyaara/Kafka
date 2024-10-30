package com.kafka.reader.epub.parser

expect object ImageDecoder {
    fun getAspectRatio(imageData: ByteArray): Float
}

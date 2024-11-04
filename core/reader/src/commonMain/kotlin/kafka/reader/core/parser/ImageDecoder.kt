package kafka.reader.core.parser

expect object ImageDecoder {
    fun getAspectRatio(imageData: ByteArray): Float
}

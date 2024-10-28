package com.kafka.reader.epub.parser

actual object EpubImageParser {
    actual fun getImagePath(para: String) =
        BookTextMapper.ImgEntry.fromXMLString(para)?.path
}

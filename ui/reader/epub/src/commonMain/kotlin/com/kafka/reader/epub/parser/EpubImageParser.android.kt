package com.kafka.reader.epub.parser

object EpubImageParser {
    fun getImagePath(para: String) =
        BookTextMapper.ImgEntry.fromXMLString(para)?.path
}

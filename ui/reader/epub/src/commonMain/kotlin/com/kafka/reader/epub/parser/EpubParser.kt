package com.kafka.reader.epub.parser

import com.kafka.reader.epub.models.EpubBook

expect class EpubParser {
    suspend fun createEpubBook(filePath: String, shouldUseToc: Boolean = true): EpubBook
}

expect object EpubImageParser {
    fun getImagePath(para: String): String?
}

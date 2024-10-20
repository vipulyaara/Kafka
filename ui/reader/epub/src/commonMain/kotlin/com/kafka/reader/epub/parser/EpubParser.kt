package com.kafka.reader.epub.parser

import com.kafka.reader.epub.models.EpubBook
import java.io.InputStream
import javax.inject.Inject

expect class EpubParser @Inject constructor() {
    suspend fun createEpubBook(inputStream: InputStream, shouldUseToc: Boolean = true): EpubBook
}

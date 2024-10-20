package com.kafka.reader.epub.parser

import com.kafka.reader.epub.models.EpubBook
import java.io.InputStream
import javax.inject.Inject

actual class EpubParser @Inject actual constructor() {
    actual suspend fun createEpubBook(inputStream: InputStream, shouldUseToc: Boolean): EpubBook {
        TODO("kmp")
    }
}

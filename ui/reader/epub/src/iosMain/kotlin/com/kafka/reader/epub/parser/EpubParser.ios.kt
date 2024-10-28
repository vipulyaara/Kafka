package com.kafka.reader.epub.parser

import com.kafka.reader.epub.models.EpubBook
import me.tatarka.inject.annotations.Inject

@Inject
actual class EpubParser {
    actual suspend fun createEpubBook(
        filePath: String,
        shouldUseToc: Boolean
    ): EpubBook {
        return EpubBook("", "", "", "", emptyList(), emptyList())
    }
}

actual object EpubImageParser {
    actual fun getImagePath(para: String): String? {
        return null
    }
}

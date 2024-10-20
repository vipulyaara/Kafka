package com.kafka.reader.epub.domain

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.ResultInteractor
import com.kafka.base.errorLog
import com.kafka.reader.epub.models.EpubBook
import com.kafka.reader.epub.parser.EpubParser
import kotlinx.coroutines.withContext
import tm.alashow.datmusic.downloader.Downloader
import javax.inject.Inject

/**
 * Parses an epub book from a given path on local storage.
 * */
class ParseEbook @Inject constructor(
    private val epubParser: EpubParser,
    private val downloader: Downloader,
    private val dispatchers: CoroutineDispatchers
) : ResultInteractor<String, EpubBook>() {
    override suspend fun doWork(params: String): EpubBook {
        return withContext(dispatchers.io) {
            val inputStream = downloader.getInputStreamFromUri(params)
                ?: throw IllegalArgumentException("Failed to open input stream for URI: $params")
            try {
                val bookSize = inputStream.available()
                debug { "Input stream opened. Available bytes: $bookSize" }
                epubParser.createEpubBook(inputStream)
            } catch (e: Exception) {
                errorLog(e) { "Unexpected error while parsing EPUB" }
                throw IllegalStateException("Failed to parse EPUB from URI: $params", e)
            } finally {
                inputStream.close()
            }
        }
    }
}

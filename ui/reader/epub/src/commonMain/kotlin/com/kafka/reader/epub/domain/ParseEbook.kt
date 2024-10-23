package com.kafka.reader.epub.domain

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.reader.epub.models.EpubBook
import com.kafka.reader.epub.parser.EpubParser
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Parses an epub book from a given path on local storage.
 * */
class ParseEbook @Inject constructor(
    private val epubParser: EpubParser,
    private val dispatchers: CoroutineDispatchers
) : Interactor<String, EpubBook>() {
    override suspend fun doWork(params: String): EpubBook {
        return withContext(dispatchers.io) {
            epubParser.createEpubBook(params)
        }
    }
}

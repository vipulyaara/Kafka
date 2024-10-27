package com.kafka.reader.epub.domain

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.reader.epub.models.EpubBook
import com.kafka.reader.epub.parser.EpubParser
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

/**
 * Parses an epub book from a given path on local storage.
 * */
@Inject
class ParseEbook(
    private val epubParser: EpubParser,
    private val dispatchers: CoroutineDispatchers
) : Interactor<String, EpubBook>() {
    override suspend fun doWork(params: String): EpubBook {
        return withContext(dispatchers.io) {
            epubParser.createEpubBook(params)
        }
    }
}

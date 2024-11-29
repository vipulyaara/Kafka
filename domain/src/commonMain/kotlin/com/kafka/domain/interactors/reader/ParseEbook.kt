package com.kafka.domain.interactors.reader

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import kafka.reader.core.models.EpubBook
import kafka.reader.core.parser.EpubParser
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okio.Source

/**
 * Parses an epub book from a given path on local storage.
 * */
@Inject
class ParseEbook(
    private val epubParser: EpubParser,
    private val dispatchers: CoroutineDispatchers
) : Interactor<ParseEbook.Params, EpubBook>() {
    override suspend fun doWork(params: Params): EpubBook {
        return withContext(dispatchers.io) {
            when {
                params.filePath != null -> epubParser.createEpubBook(
                    filePath = params.filePath,
                )

                params.source != null -> epubParser.createEpubBook(
                    source = params.source,
                )

                else -> error("File path or source must be provided")
            }
        }
    }

    data class Params(val filePath: String? = null, val source: Source? = null)
}

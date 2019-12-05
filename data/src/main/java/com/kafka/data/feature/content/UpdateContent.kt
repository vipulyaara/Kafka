package com.kafka.data.feature.content

import com.kafka.data.data.config.ProcessLifetime
import com.kafka.data.data.interactor.Interactor
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.query.booksByAuthor
import com.kafka.data.query.booksByCollection
import com.kafka.data.query.booksByGenre
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateContent @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val repository: ContentRepository
) : Interactor<UpdateContent.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    var query = ArchiveQuery()

    override suspend fun doWork(params: Params) {
        val query = when (params) {
            is Params.ByCreator -> query.booksByAuthor(params.creator)
            is Params.ByCollection -> query.booksByCollection(params.collection)
            is Params.ByGenre -> query.booksByGenre(params.genre)
        }
        repository.updateQuery(query)
    }

    sealed class Params {
        class ByCreator(val creator: String) : Params()
        class ByCollection(val collection: String) : Params()
        class ByGenre(val genre: String) : Params()
    }

    data class ExecuteParams(val id: Long = 0)
}

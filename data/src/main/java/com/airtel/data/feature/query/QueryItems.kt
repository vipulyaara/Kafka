package com.airtel.data.feature.query

import com.airtel.data.data.config.kodeinInstance
import com.airtel.data.entities.Item
import com.airtel.data.feature.SubjectInteractor
import com.airtel.data.model.data.Result
import com.airtel.data.query.ArchiveQuery
import com.airtel.data.query.booksByAuthor
import com.airtel.data.query.booksByCollection
import com.airtel.data.query.booksByGenre
import com.airtel.data.util.AppCoroutineDispatchers
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineDispatcher
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 * implementation of a query interactor using [ArchiveQuery].
 *
 */
class QueryItems(
    dispatchers: AppCoroutineDispatchers
) :
    SubjectInteractor<QueryItems.Params, QueryItems.ExecuteParams, List<Item>>() {

    override val dispatcher: CoroutineDispatcher = dispatchers.io

    var query = ArchiveQuery()
    private val repository: QueryRepository by kodeinInstance.instance()

    override fun createObservable(params: Params): Flowable<List<Item>> {
        return when (params) {
            is Params.ByCreator -> repository.observeQueryByCreator(params.creator)
            is Params.ByCollection -> repository.observeQueryByCollection(params.collection)
            is Params.ByGenre -> repository.observeQueryByGenre(params.genre)
        }
    }

    override suspend fun execute(params: Params, executeParams: ExecuteParams) {
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

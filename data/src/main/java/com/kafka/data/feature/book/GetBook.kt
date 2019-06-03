package com.kafka.data.feature.book

import com.kafka.data.entities.Book
import com.kafka.data.data.interactor.SubjectInteractor
import com.kafka.data.util.AppCoroutineDispatchers
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class GetBook constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repository: BookRepository
) : SubjectInteractor<GetBook.Params, GetBook.ExecuteParams, List<Book>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun execute(
        params: Params,
        executeParams: ExecuteParams
    ) {
        repository.updateBooks(params.path, params.searchKeyword)
    }

    override fun createObservable(params: Params): Flowable<List<Book>> {
        return repository.observeForFlowable(params.path, params.searchKeyword)
    }

    data class Params(
        val path: String,
        val searchKeyword: String
    )

    data class ExecuteParams(val id: Long = 0)
}

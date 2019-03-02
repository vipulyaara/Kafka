package com.airtel.data.feature.book

import com.airtel.data.entities.Book
import com.airtel.data.feature.SubjectInteractor
import com.airtel.data.model.data.Result
import com.airtel.data.util.AppCoroutineDispatchers
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

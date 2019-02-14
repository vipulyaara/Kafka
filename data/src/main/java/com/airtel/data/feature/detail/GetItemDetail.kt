package com.airtel.data.feature.detail

import com.airtel.data.entities.Book
import com.airtel.data.entities.ItemDetail
import com.airtel.data.feature.SubjectInteractor
import com.airtel.data.feature.book.BookRepository
import com.airtel.data.model.data.Result
import com.airtel.data.util.AppCoroutineDispatchers
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class GetItemDetail constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repository: ItemDetailRepository
) : SubjectInteractor<GetItemDetail.Params, GetItemDetail.ExecuteParams, ItemDetail>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun execute(
        params: Params,
        executeParams: ExecuteParams
    ): Result<ItemDetail> {
        return repository.updateItemDetail(params.itemId)
    }

    override fun createObservable(params: Params): Flowable<ItemDetail> {
        return repository.observeForFlowable(params.itemId)
    }

    data class Params(val itemId: String)

    data class ExecuteParams(val id: Long = 0)
}

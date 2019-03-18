package com.kafka.data.feature.detail

import com.kafka.data.entities.ItemDetail
import com.kafka.data.data.interactor.SubjectInteractor
import com.kafka.data.util.AppCoroutineDispatchers
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
    ) {
        repository.updateItemDetail(params.itemId)
    }

    override fun createObservable(params: Params): Flowable<ItemDetail> {
        return repository.observeForFlowable(params.itemId)
    }

    data class Params(val itemId: String)

    data class ExecuteParams(val id: Long = 0)
}

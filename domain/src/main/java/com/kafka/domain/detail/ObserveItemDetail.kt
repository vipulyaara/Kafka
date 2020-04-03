package com.kafka.domain.detail

import com.kafka.data.detail.ItemDetailRepository
import com.kafka.data.entities.ItemDetail
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Interactor for observing the content detail.
 * @see ItemDetailRepository
 *
 */
class ObserveItemDetail @Inject constructor(
    private val dispatchers: com.data.base.AppCoroutineDispatchers,
    private val repository: ItemDetailRepository
) : com.kafka.domain.SubjectInteractor<ObserveItemDetail.Param, ItemDetail>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Param): Flow<ItemDetail> {
        return repository.observeItemDetail(params.contentId)
    }

    data class Param(val contentId: String)
}

package com.kafka.content.domain.detail

import com.kafka.data.model.AppCoroutineDispatchers
import com.kafka.data.model.SubjectInteractor
import com.kafka.content.data.detail.ItemDetailRepository
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
    dispatchers: AppCoroutineDispatchers,
    private val repository: ItemDetailRepository
) : SubjectInteractor<ObserveItemDetail.Param, ItemDetail?>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Param): Flow<ItemDetail?> {
        return repository.observeItemDetail(params.contentId)
    }

    data class Param(val contentId: String)
}

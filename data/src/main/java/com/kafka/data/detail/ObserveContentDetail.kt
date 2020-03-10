package com.kafka.data.detail

import com.kafka.data.data.interactor.SubjectInteractor
import com.kafka.data.entities.ItemDetail
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Interactor for observing the content detail.
 * @see ContentDetailRepository
 *
 */
class ObserveContentDetail @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: ContentDetailRepository
) : SubjectInteractor<ObserveContentDetail.Param, ItemDetail>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Param): Flow<ItemDetail> {
        return repository.observeItemDetail(params.contentId)
    }

    data class Param(val contentId: String)
}

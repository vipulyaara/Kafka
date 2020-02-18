package com.kafka.data.feature.detail

import com.kafka.data.data.interactor.SubjectInteractor
import com.kafka.data.entities.ContentDetail
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
) : SubjectInteractor<ObserveContentDetail.Param, ContentDetail>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Param): Flow<ContentDetail> {
        return repository.observeItemDetail(params.contentId)
    }

    data class Param(val contentId: String)
}
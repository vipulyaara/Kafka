package com.kafka.data.feature.detail

import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.interactor.SubjectInteractor
import com.kafka.data.entities.ContentDetail
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Interactor for updating the content detail.
 * @see ContentDetailRepository
 *
 */
class ObserveContentDetail : SubjectInteractor<ObserveContentDetail.Param, ContentDetail>() {

    private val dispatchers: AppCoroutineDispatchers by kodeinInstance.instance()
    private val repository: ContentDetailRepository by kodeinInstance.instance()

    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Param): Flow<ContentDetail> {
        return repository.observeItemDetail(params.contentId)
    }

    data class Param(val contentId: String)
}
package com.kafka.data.feature.detail

import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.data.interactor.Interactor
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Interactor for updating the content detail.
 * @see ContentDetailRepository
 *
 */
class UpdateContentDetail : Interactor<UpdateContentDetail.Param>() {

    private val dispatchers: AppCoroutineDispatchers by kodeinInstance.instance()
    private val repository: ContentDetailRepository by kodeinInstance.instance()
    private val processScope: CoroutineScope by kodeinInstance.instance()

    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Param) {
        repository.updateContentDetail(params.contentId)
    }

    data class Param(val contentId: String)
}

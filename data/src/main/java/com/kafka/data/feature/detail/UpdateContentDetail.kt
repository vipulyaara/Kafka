package com.kafka.data.feature.detail

import com.kafka.data.data.config.ProcessLifetime
import com.kafka.data.data.interactor.Interactor
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Interactor for updating the content detail.
 * @see ContentDetailRepository
 *
 */
class UpdateContentDetail @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: ContentDetailRepository,
    @ProcessLifetime private val processScope: CoroutineScope
) : Interactor<UpdateContentDetail.Param>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Param) {
        repository.updateContentDetail(params.contentId)
    }

    data class Param(val contentId: String)
}

package com.kafka.domain.detail

import com.kafka.data.di.ProcessLifetime
import com.kafka.data.detail.ItemDetailRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 10/12/18.
 *
 * Interactor for updating the content detail.
 * @see ItemDetailRepository
 *
 */
class UpdateItemDetail @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    private val repository: ItemDetailRepository,
    @ProcessLifetime private val processScope: CoroutineScope
) : com.kafka.domain.Interactor<UpdateItemDetail.Param>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Param) {
        repository.updateItemDetail(params.contentId)
    }

    data class Param(val contentId: String)
}

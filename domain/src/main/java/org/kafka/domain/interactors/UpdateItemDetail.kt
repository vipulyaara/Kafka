package org.kafka.domain.interactors

import com.kafka.data.feature.item.ItemDetailDataSource
import kotlinx.coroutines.withContext
import org.kafka.analytics.Analytics
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateItemDetail @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: ItemDetailDataSource,
    private val analytics: Analytics,
) : Interactor<UpdateItemDetail.Param>() {

    override suspend fun doWork(params: Param) {
        withContext(dispatchers.io) {
            analytics.log { itemDetailClick(params.contentId) }
            repository.updateItemDetail(params.contentId).getOrThrow()
        }
    }

    data class Param(val contentId: String)
}

package org.kafka.domain.interactors

import com.kafka.data.feature.item.ItemDetailDataSource
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateItemDetail @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: ItemDetailDataSource,
) : Interactor<UpdateItemDetail.Param>() {

    override suspend fun doWork(params: Param) {
        withContext(dispatchers.io) {
            repository.updateItemDetail(params.contentId).getOrThrow()
        }
    }

    data class Param(val contentId: String)
}

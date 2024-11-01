package com.kafka.domain.interactors

import com.kafka.data.feature.item.ItemDetailDataSource
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateItemDetail @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val repository: ItemDetailDataSource,
) : Interactor<UpdateItemDetail.Param>() {

    override suspend fun doWork(params: Param) {
        withContext(dispatchers.io) {
            repository.updateItemDetail(params.contentId)
        }
    }

    data class Param(val contentId: String)
}

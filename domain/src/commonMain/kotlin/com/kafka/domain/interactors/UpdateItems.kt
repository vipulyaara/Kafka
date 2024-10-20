package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.Service
import com.kafka.base.appService
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import com.kafka.domain.interactors.query.BuildRemoteQuery
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val buildRemoteQuery: BuildRemoteQuery,
    private val itemRepository: ItemRepository,
) : Interactor<UpdateItems.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (appService == Service.Archive) {
                itemRepository.updateQuery(buildRemoteQuery(params.archiveQuery)).let {
                    itemRepository.saveItems(it)
                }
            }
        }
    }

    data class Params(val archiveQuery: ArchiveQuery)
}

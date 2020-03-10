package com.kafka.data.recent

import com.kafka.data.content.ContentRepository
import com.kafka.data.data.interactor.SubjectInteractor
import com.kafka.data.entities.Item
import com.kafka.data.query.ArchiveQuery
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveRecentlyVisitedItems @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val contentRepository: ContentRepository
) : SubjectInteractor<ObserveRecentlyVisitedItems.Params, List<Item>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<List<Item>> {
        return contentRepository.observeRecentlyVisitedItems()
    }

    data class Params(val archiveQuery: ArchiveQuery)
}

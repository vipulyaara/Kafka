package org.rekhta.domain.observers

import com.kafka.data.entities.Item
import com.kafka.data.feature.item.ItemRepository
import com.kafka.data.model.ArchiveQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import org.rekhta.base.AppCoroutineDispatchers
import org.rekhta.base.domain.SubjectInteractor
import org.rekhta.domain.interactors.query.BuildLocalQuery
import javax.inject.Inject

class ObserveQueryItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val buildLocalQuery: BuildLocalQuery,
    private val itemRepository: ItemRepository
) : SubjectInteractor<ObserveQueryItems.Params, List<Item>>() {

    override fun createObservable(params: Params): Flow<List<Item>> {
        return itemRepository.observeQueryItems(buildLocalQuery(params.archiveQuery))
            .flowOn(dispatchers.io)
    }

    data class Params(val archiveQuery: ArchiveQuery)
}

package org.kafka.domain.observers

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveCreatorItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeQueryItems: ObserveQueryItems,
    private val itemDao: ItemDao
) : SubjectInteractor<ObserveCreatorItems.Params, List<Item>>() {

    override fun createObservable(params: Params): Flow<List<Item>> {
        return itemDao.observe(params.itemId)
            .flowOn(dispatchers.io)
            .flatMapLatest {
                it?.creator?.name?.let { creator ->
                    val query = ArchiveQuery().booksByAuthor(creator)
                    observeQueryItems.execute(ObserveQueryItems.Params(query))
                } ?: emptyFlow()
            }
    }

    data class Params(val itemId: String)
}

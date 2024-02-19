package org.kafka.domain.observers

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByAuthor
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveCreatorItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeQueryItems: ObserveQueryItems,
    private val itemDao: ItemDao,
) : SubjectInteractor<ObserveCreatorItems.Params, ImmutableList<Item>>() {

    override fun createObservable(params: Params): Flow<ImmutableList<Item>> {
        return itemDao.observe(params.itemId)
            .flowOn(dispatchers.io)
            .flatMapLatest {
                it?.creator?.name?.let { creator ->
                    val query = ArchiveQuery().booksByAuthor(creator)
                    observeQueryItems.execute(ObserveQueryItems.Params(query))
                } ?: flowOf(emptyList())
            }
            .map { it.filterNot { it.itemId == params.itemId }.toPersistentList() }
    }

    data class Params(val itemId: String)
}

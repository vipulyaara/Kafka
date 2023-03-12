package org.kafka.domain.interactors.recent

import com.kafka.data.dao.ItemDao
import com.kafka.data.model.ArchiveQuery
import com.kafka.data.model.booksByIdentifiers
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import org.kafka.domain.interactors.UpdateItems
import javax.inject.Inject

class UpdateFavoriteItems @Inject constructor(
    private val updateItems: UpdateItems,
    private val dispatchers: AppCoroutineDispatchers,
    private val itemDao: ItemDao
) : Interactor<UpdateFavoriteItems.Params>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val items = itemDao.get(params.itemIds)
            val unfetchedItems = params.itemIds - items.map { it.itemId }.toSet()

            if (unfetchedItems.isNotEmpty()) {
                val query = ArchiveQuery().apply {
                    booksByIdentifiers(unfetchedItems)
                }
                updateItems.execute(UpdateItems.Params(query))
            }
        }
    }

    data class Params(val itemIds: List<String>)
}

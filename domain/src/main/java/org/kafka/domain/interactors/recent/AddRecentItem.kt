package org.kafka.domain.interactors.recent

import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.RecentItem
import kotlinx.coroutines.withContext
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import org.kafka.domain.interactors.UpdateRecentItem
import javax.inject.Inject

class AddRecentItem @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val itemDetailDao: ItemDetailDao,
    private val updateRecentItem: UpdateRecentItem
) : Interactor<AddRecentItem.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val item = itemDetailDao.get(params.itemId)
            updateRecentItem.execute(RecentItem.fromItem(item))
        }
    }

    data class Params(val itemId: String)
}

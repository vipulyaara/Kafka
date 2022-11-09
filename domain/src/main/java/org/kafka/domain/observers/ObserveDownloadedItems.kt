package org.kafka.domain.observers

import com.kafka.data.dao.ItemDao
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.Item
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveDownloadedItems @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val itemDao: ItemDao,
    private val itemDetailDao: ItemDetailDao
) : SubjectInteractor<Unit, List<Item>>() {

    override fun createObservable(params: Unit): Flow<List<Item>> {
        return itemDetailDao.itemDetailFlow()
            .map { itemDetails ->
                itemDetails.filter { itemDetail ->
                    itemDetail.files?.any { it.localUri != null } ?: false
                }
            }
            .map { itemDetails ->
                itemDetails.map { itemDao.getItemByItemId(it.itemId) }
            }.flowOn(dispatchers.io)
    }
}

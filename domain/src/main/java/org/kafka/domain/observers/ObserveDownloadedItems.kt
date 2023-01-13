package org.kafka.domain.observers

import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
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
    private val fileDao: FileDao
) : SubjectInteractor<Unit, List<Item>>() {

    override fun createObservable(params: Unit): Flow<List<Item>> {
        return fileDao.observeDownloadedFiles()
            .map { files ->
                files.map { itemDao.getItemByItemId(it.itemId) }
                    .distinctBy { it.itemId }
                    .asReversed()
            }.flowOn(dispatchers.io)
    }
}

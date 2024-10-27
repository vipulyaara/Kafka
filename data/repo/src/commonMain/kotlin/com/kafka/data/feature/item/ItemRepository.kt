package com.kafka.data.feature.item

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import me.tatarka.inject.annotations.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
@Inject
class ItemRepository(
    private val itemDao: ItemDao
) {
    suspend fun saveItems(items: List<Item>) = itemDao.insertAll(items)

    suspend fun exists(id: String) = itemDao.exists(id)
}

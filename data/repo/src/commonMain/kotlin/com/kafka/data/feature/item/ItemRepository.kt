package com.kafka.data.feature.item

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.Item
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemRepository @Inject constructor(
    private val itemDao: ItemDao
) {
    suspend fun saveItems(items: List<Item>) = itemDao.insertAll(items)

    suspend fun exists(id: String) = itemDao.exists(id)
}

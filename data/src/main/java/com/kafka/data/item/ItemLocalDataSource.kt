package com.kafka.data.item

import com.kafka.data.data.db.dao.ItemDao
import com.kafka.data.entities.Item
import com.kafka.data.recent.RecentItemDao
import com.kafka.data.entities.RecentItem
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ItemLocalDataSource @Inject constructor(
    private val itemDao: ItemDao,
    private val recentItemDao: RecentItemDao
) {

    fun getItemByItemId(itemId: String) = itemDao.getItemByItemId(itemId)

    fun observeQueryItems() = itemDao.observeQueryItems()

    fun observeRecentlyVisitedItems() = recentItemDao.observeRecentlyVisitedItems()

    suspend fun saveRecentlyVisitedItem(item: RecentItem) = recentItemDao.insert(item)

    suspend fun saveItems(items: List<Item>) = itemDao.insertAll(items)
}

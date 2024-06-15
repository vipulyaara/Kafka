package com.kafka.data.feature.homepage

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.model.homepage.HomepageCollectionResponse
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomepageMapper @Inject constructor(private val itemDao: ItemDao) {
    fun map(collection: List<HomepageCollectionResponse>) = combine(
        collection.map {
            when (it) {
                is HomepageCollectionResponse.Row -> it.mapRows()
                is HomepageCollectionResponse.Column -> it.mapColumn()
                is HomepageCollectionResponse.FeaturedItem -> it.mapFeatured()
                is HomepageCollectionResponse.RecentItems -> it.mapRecentItems()
                is HomepageCollectionResponse.Grid -> it.mapGrid()
                is HomepageCollectionResponse.PersonRow -> it.mapPersonRow()
                is HomepageCollectionResponse.Unknown -> flowOf(null)
            }
        },
    ) { it.filterNotNull().toList() }

    private fun HomepageCollectionResponse.Row.mapRows(): Flow<HomepageCollection.Row> {
        val itemIdList = itemIds.split(", ")
        return itemDao.observe(itemIdList).map { items ->
            val sortedItems = items.sortedBy { item -> itemIdList.indexOf(item.itemId) }
            HomepageCollection.Row(
                labels = label.splitLabel().toPersistentList(),
                items = sortedItems.toPersistentList(),
                clickable = clickable,
            )
        }
    }

    private fun HomepageCollectionResponse.PersonRow.mapPersonRow(): Flow<HomepageCollection.PersonRow> {
        val itemIdList = itemIds.split(", ")

        val personRow = HomepageCollection.PersonRow(
            items = itemIdList.toPersistentList(),
            images = image.map { it.downloadURL }.toPersistentList(),
            enabled = enabled,
        )

        return flowOf(personRow)
    }

    private fun HomepageCollectionResponse.Column.mapColumn(): Flow<HomepageCollection.Column> {
        val itemIdList = itemIds.split(", ")
        return itemDao.observe(itemIdList).map { items ->
            val sortedItems = items.sortedBy { item -> itemIdList.indexOf(item.itemId) }
            HomepageCollection.Column(
                labels = label.splitLabel().toPersistentList(),
                items = sortedItems.toPersistentList(),
                clickable = clickable,
            )
        }
    }

    private fun HomepageCollectionResponse.Grid.mapGrid(): Flow<HomepageCollection.Grid> {
        val itemIdList = itemIds.split(", ")
        return itemDao.observe(itemIdList).map { items ->
            val sortedItems = items.sortedBy { item -> itemIdList.indexOf(item.itemId) }
            HomepageCollection.Grid(
                labels = label.splitLabel().toPersistentList(),
                items = sortedItems.toPersistentList(),
                clickable = clickable,
            )
        }
    }

    // todo: items are filled later on domain layer, find a way to fill them here
    private fun HomepageCollectionResponse.RecentItems.mapRecentItems() =
        flowOf(HomepageCollection.RecentItems(persistentListOf(), enabled))

    private fun HomepageCollectionResponse.FeaturedItem.mapFeatured() =
        itemDao.observe(itemIds.split(", ")).map { items ->
            val itemIdsIndexMap = itemIds.split(", ")
                .withIndex()
                .associate { it.value to it.index }
            val sortedItems = items.sortedBy { itemIdsIndexMap[it.itemId] ?: Int.MAX_VALUE }

            HomepageCollection.FeaturedItem(
                label = label,
                items = sortedItems.toPersistentList(),
                image = image?.map { it.downloadURL }?.toPersistentList() ?: persistentListOf(),
                enabled = enabled,
            )
        }

    private fun String?.splitLabel(separator: String = ", ") =
        this.orEmpty().split(separator).filter { it.isNotEmpty() }
}

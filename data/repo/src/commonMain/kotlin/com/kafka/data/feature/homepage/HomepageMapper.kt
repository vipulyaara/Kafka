package com.kafka.data.feature.homepage

import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.HomepageCollection
import com.kafka.data.entities.RecentItemWithProgress
import com.kafka.data.feature.RecentItemRepository
import com.kafka.data.feature.homepage.HomepageMapperConfig.shuffleIndices
import com.kafka.data.feature.recommendation.RecommendationRepository
import com.kafka.data.model.homepage.HomepageCollectionResponse
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isRecommendationRowEnabled
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

object HomepageMapperConfig {
    val shuffleIndices = (0 until 50).shuffled()
}

class HomepageMapper @Inject constructor(
    private val itemDao: ItemDao,
    private val remoteConfig: RemoteConfig,
    private val auth: FirebaseAuth,
    private val recommendationRepository: RecommendationRepository,
    private val recentItemRepository: RecentItemRepository
) {

    fun map(collection: List<HomepageCollectionResponse>): Flow<List<HomepageCollection>> {
        return combine(
            collection.map { homepageItem ->
                when (homepageItem) {
                    is HomepageCollectionResponse.Row -> homepageItem.mapRows()
                    is HomepageCollectionResponse.Column -> homepageItem.mapColumn()
                    is HomepageCollectionResponse.FeaturedItem -> homepageItem.mapFeatured()
                    is HomepageCollectionResponse.RecentItems -> mapRecentItems()
                    is HomepageCollectionResponse.Grid -> homepageItem.mapGrid()
                    is HomepageCollectionResponse.PersonRow -> homepageItem.mapPersonRow()
                    is HomepageCollectionResponse.Subjects -> homepageItem.mapSubjects()
                    is HomepageCollectionResponse.Recommendation ->
                        if (remoteConfig.isRecommendationRowEnabled() && auth.currentUser != null) {
                            homepageItem.mapRecommendations()
                        } else {
                            flowOf(null)
                        }

                    is HomepageCollectionResponse.Unknown -> flowOf(null)
                }
            },
        ) { homepageItems -> homepageItems.filterNotNull().toList() }
    }

    private fun HomepageCollectionResponse.Row.mapRows(): Flow<HomepageCollection.Row> {
        val itemIdList = itemIds.split(", ")
            .let { if (shuffle) it.shuffleInSync() else it }

        return itemDao.observe(itemIdList).map { items ->
            val sortedItems = items.sortedBy { item -> itemIdList.indexOf(item.itemId) }
            HomepageCollection.Row(
                labels = label.splitLabel(),
                items = sortedItems,
                clickable = clickable,
                shuffle = shuffle
            )
        }
    }

    private fun HomepageCollectionResponse.Recommendation.mapRecommendations() =
        recommendationRepository.observeRecommendations(itemIds)
            .map { items ->
                HomepageCollection.Recommendations(
                    labels = listOf(label),
                    type = itemIds,
                    items = if (shuffle) items.shuffleInSync() else items
                )
            }

    private fun HomepageCollectionResponse.PersonRow.mapPersonRow(): Flow<HomepageCollection.PersonRow> {
        val indices = itemIds.split(", ").indices
            .let { if (shuffle) it.shuffleInSync() else it }
        val itemIdList = indices.map { index -> itemIds.split(", ")[index] }
        val images = indices.map { index -> image.getOrNull(index) }

        val personRow = HomepageCollection.PersonRow(
            items = itemIdList,
            images = images.mapNotNull { it?.downloadURL },
            clickable = clickable,
            shuffle = shuffle
        )

        return flowOf(personRow)
    }

    private fun HomepageCollectionResponse.Subjects.mapSubjects(): Flow<HomepageCollection.Subjects> {
        val itemIdList = itemIds.split(", ")
            .let { if (shuffle) it.shuffleInSync() else it }

        val subjects = HomepageCollection.Subjects(
            items = itemIdList,
            clickable = clickable,
            shuffle = shuffle
        )

        return flowOf(subjects)
    }

    private fun HomepageCollectionResponse.Column.mapColumn(): Flow<HomepageCollection.Column> {
        val itemIdList = itemIds.split(", ")
            .let { if (shuffle) it.shuffleInSync() else it }

        return itemDao.observe(itemIdList).map { items ->
            val sortedItems = items.sortedBy { item -> itemIdList.indexOf(item.itemId) }
            HomepageCollection.Column(
                labels = label.splitLabel(),
                items = sortedItems,
                clickable = clickable,
                shuffle = shuffle
            )
        }
    }

    private fun HomepageCollectionResponse.Grid.mapGrid(): Flow<HomepageCollection.Grid> {
        val itemIdList = itemIds.split(", ")
            .let { if (shuffle) it.shuffleInSync() else it }

        return itemDao.observe(itemIdList).map { items ->
            val sortedItems = items.sortedBy { item -> itemIdList.indexOf(item.itemId) }
            HomepageCollection.Grid(
                labels = label.splitLabel(),
                items = sortedItems,
                clickable = clickable,
                shuffle = shuffle
            )
        }
    }

    private fun mapRecentItems() = recentItemRepository.observeRecentItems(RECENT_ITEMS_LIMIT)
        .map { it.map { RecentItemWithProgress(it, 0) } }
        .map { items -> HomepageCollection.RecentItems(items) }

    private fun HomepageCollectionResponse.FeaturedItem.mapFeatured(): Flow<HomepageCollection.FeaturedItem> {
        val indices = itemIds.split(", ").indices
            .let { if (shuffle) it.shuffleInSync() else it }

        val sortedItemIds = indices.map { index -> itemIds.split(", ")[index] }
        val images = indices.map { index -> image?.getOrNull(index) }

        return itemDao.observe(sortedItemIds).map { items ->
            val itemIdsIndexMap = sortedItemIds
                .withIndex()
                .associate { it.value to it.index }
            val sortedItems = items.sortedBy { itemIdsIndexMap[it.itemId] ?: Int.MAX_VALUE }

            HomepageCollection.FeaturedItem(
                label = label,
                items = sortedItems,
                image = images.mapNotNull { it?.downloadURL }.toImmutableList(),
                shuffle = shuffle
            )
        }
    }

    private fun String?.splitLabel(separator: String = ", ") =
        this.orEmpty().split(separator).filter { it.isNotEmpty() }

    private fun <T> Iterable<T>.shuffleInSync(): List<T> {
        return withIndex()
            .sortedBy { (index, _) -> shuffleIndices.getOrElse(index) { index } }
            .map { it.value }
    }
}

private const val RECENT_ITEMS_LIMIT = 10

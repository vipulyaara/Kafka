package com.kafka.data.feature

import com.google.firebase.firestore.Query
import com.kafka.base.ApplicationScope
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class RecentItemRepository(
    private val firestoreGraph: FirestoreGraph
) {
    suspend fun getRecentItems(uid: String): List<RecentItem> {
        return firestoreGraph.readingListCollection(uid)
            .get()
            .documents.map { it.data() }
    }

    fun observeRecentItems(uid: String, limit: Int) =
        firestoreGraph.readingListCollection(uid)
            .orderBy("updated_at", Query.Direction.DESCENDING)
            .limit(limit)
            .snapshots
            .map { snapshot ->
                snapshot.documents.map { it.data<RecentItem>() }
            }

//    suspend fun getRecentItems(uid: String): List<RecentItem> {
//        return supabase.recentItems
//            .select(Columns.raw(RecentItemSchema.joinedColumns)) {
//                filter { CurrentlyReading::uid eq uid }
//                order("updated_at", Order.DESCENDING)
//            }.decodeList<RecentItemSchema>().map { recent ->
//                RecentItem(
//                    itemId = recent.itemId,
//                    fileId = recent.fileId,
//                    title = recent.item.title,
//                    coverUrl = recent.item.coverImage,
//                    creators = recent.item.creators,
//                    mediaType = recent.item.mediaType,
//                    updatedAt = Clock.System.now()
//                )
//            }
//    }

//    fun observeRecentItems(uid: String, limit: Int) =
//        supabase.recentItems.safeSelectAsFlow(
//            primaryKeys = listOf(CurrentlyReading::itemId, CurrentlyReading::uid),
//            filter = FilterOperation("uid", FilterOperator.EQ, uid)
//        ).map { readingList ->
//            supabase.items.select {
//                filter { Item::itemId isIn readingList.map { it.itemId } }
//            }.decodeList<Item>().map { item ->
//                val listItem = readingList.first { it.itemId == item.itemId }
//                RecentItem(
//                    fileId = listItem.fileId,
//                    itemId = listItem.itemId,
//                    title = item.title,
//                    coverUrl = item.coverImage.orEmpty(),
//                    creators = item.creators,
//                    updatedAt = Clock.System.now()
//                )
//            }.sortedBy { it.updatedAt }
//        }
}

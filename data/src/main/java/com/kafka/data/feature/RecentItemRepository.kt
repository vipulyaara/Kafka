package com.kafka.data.feature

import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.snapshots
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.firestore.FirestoreGraph
import dagger.Reusable
import kotlinx.coroutines.flow.map
import org.kafka.base.debug
import javax.inject.Inject

@Reusable
class RecentItemRepository @Inject constructor(private val firestoreGraph: FirestoreGraph) {
    fun observeRecentItems(uid: String) = firestoreGraph.getRecentItemsCollection(uid)
        .snapshots()
        .map { snapshot ->
            snapshot.map { mapRecentItem(it) }
                .sortedByDescending { it.createdAt }
                .distinctBy { it.itemId }
                .also { debug { "recent are $it" } }
        }

    private fun mapRecentItem(queryDocumentSnapshot: QueryDocumentSnapshot): RecentItem {
        val mediaType = queryDocumentSnapshot.getString("mediaType")
        return if (mediaType == "texts") {
            queryDocumentSnapshot.toObject(RecentItem.Readable::class.java)
        } else {
            queryDocumentSnapshot.toObject(RecentItem.Listenable::class.java)
        }
    }
}

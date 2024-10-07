package com.kafka.data.feature

import com.google.firebase.firestore.Query
import com.kafka.base.ApplicationScope
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class RecentItemRepository @Inject constructor(private val firestoreGraph: FirestoreGraph) {
    fun observeRecentItems(uid: String, limit: Int) =
        firestoreGraph.getRecentItemsCollection(uid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .snapshots
            .map { snapshot ->
                snapshot.documents
                    .map { it.data<RecentItem>() }
                    .distinctBy { it.itemId }
            }
}

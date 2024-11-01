package com.kafka.domain.interactors

import com.kafka.base.domain.Interactor
import com.kafka.data.entities.RecentItem
import com.kafka.data.feature.firestore.FirestoreGraph
import javax.inject.Inject

class UpdateRecentItem @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
) : Interactor<RecentItem>() {
    override suspend fun doWork(params: RecentItem) {
        val document = firestoreGraph.recentItemsCollection.document(params.fileId)
        document.set(params)
    }
}

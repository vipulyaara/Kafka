package com.kafka.domain.interactors.recent

import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.base.domain.Interactor
import javax.inject.Inject

class RemoveRecentItem @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
) : Interactor<String>() {
    override suspend fun doWork(params: String) {
        val document = firestoreGraph.recentItemsCollection.document(params)
        document.delete()
    }
}

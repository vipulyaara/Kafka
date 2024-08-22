package com.kafka.data.feature.ai

import com.kafka.data.entities.Summary
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SummaryRepository @Inject constructor(private val firestoreGraph: FirestoreGraph) {

    fun observeSummary(itemId: String) = firestoreGraph.summaryCollection
        .document(itemId)
        .snapshots()
        .map { snapshots -> snapshots.data<Summary?>() }

    suspend fun updateSummary(summary: Summary) {
        firestoreGraph.summaryCollection.document(summary.itemId).set(summary)
    }
}

package com.kafka.data.feature.ai

import com.kafka.data.entities.Summary
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.map
import com.kafka.base.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class SummaryRepository @Inject constructor(private val firestoreGraph: FirestoreGraph) {

    fun observeSummary(itemId: String) = firestoreGraph.summaryCollection
        .document(itemId)
        .snapshots()
        .map { snapshots -> snapshots.data<Summary?>() }

    suspend fun updateSummary(summary: Summary) {
        firestoreGraph.summaryCollection.document(summary.itemId).set(summary)
    }
}

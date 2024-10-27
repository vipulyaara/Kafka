package com.kafka.data.feature.ai

import com.kafka.base.ApplicationScope
import com.kafka.data.entities.Summary
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class SummaryRepository(private val firestoreGraph: FirestoreGraph) {

    fun observeSummary(itemId: String) = firestoreGraph.summaryCollection
        .document(itemId)
        .snapshots()
        .map { snapshots -> snapshots.data<Summary?>() }

    suspend fun updateSummary(summary: Summary) {
        firestoreGraph.summaryCollection.document(summary.itemId).set(summary)
    }
}

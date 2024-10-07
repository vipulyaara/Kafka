package com.kafka.domain.interactors.recent

import com.kafka.base.domain.Interactor
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RemoveAllRecentItems @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
) : Interactor<Unit>() {
    override suspend fun doWork(params: Unit) {
        val documentIds = firestoreGraph.recentItemsCollection
            .snapshots()
            .map { it.documents.map { document -> document.id } }
            .first()

        documentIds.forEach { documentId ->
            firestoreGraph.recentItemsCollection.document(documentId).delete()
        }
    }
}

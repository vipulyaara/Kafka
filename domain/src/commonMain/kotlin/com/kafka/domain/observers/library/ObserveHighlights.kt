package com.kafka.domain.observers.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.feature.firestore.FirestoreGraph
import kafka.reader.core.models.TextHighlight
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveHighlights(
    private val firestoreGraph: FirestoreGraph,
    private val dispatchers: CoroutineDispatchers,
) : SubjectInteractor<ObserveHighlights.Params, List<TextHighlight>>() {

    override fun createObservable(params: Params): Flow<List<TextHighlight>> {
        val collection = firestoreGraph.highlightCollection()

        if (params.bookId != null) {
            collection.where { "bookId" equalTo params.bookId }
        }

        return collection.snapshots
            .map { it.documents }
            .map { it.map { it.data<TextHighlight>() } }
            .flowOn(dispatchers.io)
    }

    data class Params(val bookId: String?)
}

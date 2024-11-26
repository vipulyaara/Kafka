package com.kafka.domain.interactors.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.feature.firestore.FirestoreGraph
import kafka.reader.core.models.TextHighlight
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class AddHighlight(
    private val firestoreGraph: FirestoreGraph,
    private val dispatchers: CoroutineDispatchers
) : Interactor<AddHighlight.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            firestoreGraph.highlightCollection()
                .add(params.highlight)
        }
    }

    data class Params(val highlight: TextHighlight)
}

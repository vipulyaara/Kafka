@file:OptIn(ExperimentalUuidApi::class)

package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Report
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Inject
class ReportContent(
    private val firestoreGraph: FirestoreGraph,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<ReportContent.Params, Unit>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val report = Report(email = params.email, type = "content", text = params.text)

            firestoreGraph.reportsCollection
                .document(Uuid.random().toString())
                .set(report)
        }
    }

    data class Params(val text: String, val email: String)
}

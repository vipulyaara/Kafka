package com.kafka.domain.interactors

import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import dev.gitlive.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UpdateFeedback @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val auth: FirebaseAuth,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<UpdateFeedback.Params>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val email = params.email ?: auth.currentUser?.email
            firestoreGraph.feedbackCollection.document.set(
                mapOf("text" to params.text, "email" to email),
            )
        }
    }

    data class Params(val text: String, val email: String? = null)
}

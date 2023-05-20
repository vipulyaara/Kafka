package org.kafka.domain.interactors

import com.google.firebase.auth.FirebaseAuth
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class UpdateFeedback @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val auth: FirebaseAuth,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateFeedback.Params>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val email = params.email ?: auth.currentUser?.email
            firestoreGraph.feedbackCollection.document.set(
                mapOf("text" to params.text, "email" to email)
            )
        }
    }

    data class Params(val text: String, val email: String? = null)
}

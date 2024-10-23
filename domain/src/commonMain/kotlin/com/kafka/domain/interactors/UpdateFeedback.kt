package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Feedback
import com.kafka.data.feature.Supabase
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateFeedback @Inject constructor(
    private val supabase: Supabase,
    private val auth: FirebaseAuth,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<UpdateFeedback.Params, Unit>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val email = params.email ?: auth.currentUser?.email
            val feedback = Feedback(email = email, text = params.text)

            supabase.feedback.insert(feedback)
        }
    }

    data class Params(val text: String, val email: String? = null)
}

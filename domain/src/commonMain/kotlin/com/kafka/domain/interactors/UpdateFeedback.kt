package com.kafka.domain.interactors

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.entities.Feedback
import com.kafka.data.feature.Supabase
import com.kafka.data.feature.auth.AccountRepository
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateFeedback(
    private val supabase: Supabase,
    private val accountRepository: AccountRepository,
    private val dispatchers: CoroutineDispatchers,
) : Interactor<UpdateFeedback.Params, Unit>() {
    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val email = params.email ?: accountRepository.currentUser.email
            val feedback = Feedback(email = email, text = params.text)

            supabase.feedback.insert(feedback)
        }
    }

    data class Params(val text: String, val email: String? = null)
}

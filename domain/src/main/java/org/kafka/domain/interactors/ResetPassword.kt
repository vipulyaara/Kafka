package org.kafka.domain.interactors

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class ResetPassword @Inject constructor(
    private val auth: FirebaseAuth,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<ResetPassword.Params>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            auth.sendPasswordResetEmail(params.email).await()
        }
    }

    data class Params(val email: String)
}

package org.kafka.domain.interactors.account

import android.app.Application
import android.content.Intent
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.kafka.analytics.logger.Analytics
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class HandleGoogleCredentials @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val dispatchers: CoroutineDispatchers,
    private val analytics: Analytics,
    private val application: Application
) : Interactor<Intent?>() {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override suspend fun doWork(intent: Intent?) {
        withContext(dispatchers.io) {
            //todo: logout with oneTap
            val sigInClient = Identity.getSignInClient(application)
            val account = sigInClient.getSignInCredentialFromIntent(intent)
            val credentials = GoogleAuthProvider.getCredential(account.googleIdToken, null)

            firebaseAuth.signInWithCredential(credentials).await()
                ?: error("Failed to sign in with google")

            analytics.log { login("google") }
        }
    }
}

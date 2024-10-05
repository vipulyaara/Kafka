package com.kafka.domain.interactors.account

import android.app.Application
import android.content.Intent
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.GoogleAuthProvider
import com.kafka.analytics.logger.Analytics
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import dev.gitlive.firebase.auth.AuthCredential
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.withContext
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

            firebaseAuth.signInWithCredential(AuthCredential(credentials))

            analytics.log { login("google") }
        }
    }
}

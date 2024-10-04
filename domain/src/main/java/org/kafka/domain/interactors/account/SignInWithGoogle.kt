package org.kafka.domain.interactors.account

import android.app.Application
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.SecretsProvider
import com.kafka.base.domain.ResultInteractor
import javax.inject.Inject

class SignInWithGoogle @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val application: Application,
    private val secretsProvider: SecretsProvider
) : ResultInteractor<Unit, IntentSender>() {

    override suspend fun doWork(params: Unit): IntentSender {
        return withContext(dispatchers.io) {
            val sigInClient = Identity.getSignInClient(application)

            val tokenRequest = GoogleIdTokenRequestOptions.Builder()
                .setSupported(true)
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(secretsProvider.googleServerClientId.orEmpty())
                .build()

            val signInRequest = BeginSignInRequest.Builder()
                .setGoogleIdTokenRequestOptions(tokenRequest)
                .setAutoSelectEnabled(true)
                .build()

            val result = sigInClient.beginSignIn(signInRequest).await()
            result.pendingIntent.intentSender
        }
    }
}

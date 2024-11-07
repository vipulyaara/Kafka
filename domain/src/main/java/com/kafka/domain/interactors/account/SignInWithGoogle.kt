package com.kafka.domain.interactors.account

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.SecretsProvider
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.GoogleAuthProvider
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.withContext

actual class SignInWithGoogle (
    private val dispatchers: CoroutineDispatchers,
    private val secretsProvider: SecretsProvider
) {
     actual suspend operator fun invoke(params: Any?): Result<Unit> {
        return withContext(dispatchers.io) {
            val context = params as Context
            try {
                val credential = CredentialManager.create(context)
                    .getCredential(
                        context = context,
                        request = getCredentialRequest()
                    ).credential

                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val authCredential =
                        GoogleAuthProvider.credential(googleIdTokenCredential.idToken, null)
                    Firebase.auth.signInWithCredential(authCredential)
                    Result.success(Unit)
                } else {
                    throw RuntimeException("Received an invalid credential type")
                }
            } catch (e: GetCredentialCancellationException) {
                Result.failure(Exception("Sign-in was canceled. Please try again."))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun getCredentialRequest(): GetCredentialRequest {
        val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(true)
            .setServerClientId(secretsProvider.googleServerClientId.orEmpty())
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }
}

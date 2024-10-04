package com.kafka.data.feature.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.tasks.await
import com.kafka.base.ApplicationScope
import javax.inject.Inject

/**
 * Account management has complex rules specifically due to anonymous authentication.
 * The app lets users sign in anonymously and then link their account to an email/password.
 * Anonymous users can keep their data when they link their account.
 * */
@ApplicationScope
class AccountRepository @Inject constructor(
    private val auth: FirebaseAuth,
) {
    val currentFirebaseUser
        get() = auth.currentUser

    val isUserLoggedIn
        get() = auth.currentUser != null

    suspend fun signInAnonymously(): AuthResult? = auth.signInAnonymously().await()

    suspend fun signUpOrLinkUser(email: String, password: String): AuthResult? {
        return if (currentFirebaseUser != null) {
            val credential = EmailAuthProvider.getCredential(email, password)
            currentFirebaseUser?.linkWithCredential(credential)?.await()
        } else {
            auth.createUserWithEmailAndPassword(email, password).await()
        }
    }

    suspend fun signInUser(email: String, password: String): AuthResult? =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    fun observeCurrentFirebaseUser() = callbackFlow {
        val listener: (FirebaseAuth) -> Unit = { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }

        auth.addAuthStateListener(listener)

        awaitClose {
            auth.removeAuthStateListener(listener)
        }
    }.onStart { emit(null) }

    fun signOut() {
        auth.signOut()
    }

    suspend fun updateUser(name: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        currentFirebaseUser!!.updateProfile(profileUpdates).await()
    }
}

package com.kafka.data.feature.auth

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Account management has complex rules specifically due to anonymous authentication.
 * The app lets users sign in anonymously and then link their account to an email/password.
 * Anonymous users can keep their data when they link their account.
 * */
@Singleton
class AccountRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestoreGraph: FirestoreGraph
) {
    private val currentFirebaseUser
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

        awaitClose { auth.removeAuthStateListener(listener) }
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun updateUser(name: String) {
        val profileUpdates = userProfileChangeRequest {
            displayName = name
        }
        currentFirebaseUser!!.updateProfile(profileUpdates).await()
    }

    suspend fun updateFavorite(itemId: String, isFavorite: Boolean) {
        currentFirebaseUser?.uid
            ?.let { firestoreGraph.getFavoritesCollection(it) }
            ?.run {
                if (isFavorite) {
                    document(itemId).set(itemId)
                } else {
                    document(itemId).delete()
                }
            }?.await()
    }
}

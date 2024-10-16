package com.kafka.data.feature.auth

import com.kafka.base.ApplicationScope
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.EmailAuthProvider
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.onStart
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

    suspend fun signInAnonymously(): AuthResult = auth.signInAnonymously()

    suspend fun signUpOrLinkUser(email: String, password: String): AuthResult? {
        return if (currentFirebaseUser != null) {
            val credential = EmailAuthProvider.credential(email, password)
            currentFirebaseUser?.linkWithCredential(credential)
        } else {
            auth.createUserWithEmailAndPassword(email, password)
        }
    }

    suspend fun signInUser(email: String, password: String): AuthResult =
        auth.signInWithEmailAndPassword(email, password)

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    fun observeCurrentFirebaseUser() = auth.authStateChanged
        .onStart { emit(null) }

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun updateUser(name: String) {
        currentFirebaseUser!!.updateProfile(displayName = name)
    }
}

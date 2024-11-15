package com.kafka.data.feature.auth

import com.kafka.base.ApplicationScope
import dev.gitlive.firebase.auth.AuthResult
import dev.gitlive.firebase.auth.EmailAuthProvider
import dev.gitlive.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.onStart
import me.tatarka.inject.annotations.Inject

/**
 * Account management has complex rules specifically due to anonymous authentication.
 * The app lets users sign in anonymously and then link their account to an email/password.
 * Anonymous users can keep their data when they link their account.
 * */
@ApplicationScope
@Inject
class AccountRepository(private val auth: FirebaseAuth) {
    val currentUserOrNull
        get() = auth.currentUser

    private val currentUser
        get() = currentUserOrNull!!

    val currentUserId
        get() = currentUser.uid

    suspend fun signInAnonymously(): AuthResult = auth.signInAnonymously()

    suspend fun signUpOrLinkUser(email: String, password: String): AuthResult? {
        return if (currentUserOrNull != null) {
            val credential = EmailAuthProvider.credential(email, password)
            currentUserOrNull?.linkWithCredential(credential)
        } else {
            auth.createUserWithEmailAndPassword(email, password)
        }
    }

    suspend fun signIn(email: String, password: String): AuthResult =
        auth.signInWithEmailAndPassword(email, password)

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email)
    }

    fun observeCurrentUserOrNull() = auth.authStateChanged
        .onStart { emit(null) }

    fun observeCurrentUser() = observeCurrentUserOrNull()
        .filterNotNull()

    suspend fun signOut() {
        auth.signOut()
    }

    suspend fun updateUser(name: String) {
        currentUserOrNull!!.updateProfile(displayName = name)
    }
}

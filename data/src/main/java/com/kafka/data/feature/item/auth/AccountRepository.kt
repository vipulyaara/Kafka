package com.kafka.data.feature.item.auth

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.kafka.data.entities.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val currentFirebaseUser by lazy { auth.currentUser }
    private val userDocument
        get() = currentFirebaseUser?.uid?.let { usersReference(it) }

    suspend fun getCurrentUser(): User? {
        return userDocument?.get()?.await()?.toObject(User::class.java)
    }

    fun observeCurrentUser() = observeCurrentFirebaseUser()
        .flatMapLatest {
            if (it == null) {
                flowOf(null)
            } else {
                usersReference(it.uid).snapshots().map { it.toObject(User::class.java) }
            }
        }

    suspend fun signInAnonymously() =
        auth.signInAnonymously().await()?.user?.let { mapFirebaseUser(it) }

    suspend fun signUpOrLinkUser(email: String, password: String): User? {
        val authResult = if (currentFirebaseUser != null) {
            val credential = EmailAuthProvider.getCredential(email, password)
            currentFirebaseUser!!.linkWithCredential(credential)
        } else {
            auth.createUserWithEmailAndPassword(email, password)
        }.await()

        return authResult.user?.let { mapFirebaseUser(it) }
    }

    suspend fun signInUser(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password)
            .await().user?.let { mapFirebaseUser(it) }

    suspend fun updateUser(user: User) {
        userDocument!!.set(user).await()
    }

    suspend fun updateFavorite(itemId: String, isFavorite: Boolean) {
        userDocument!!.run {
            if (isFavorite) {
                update("favorites", FieldValue.arrayUnion(itemId))
            } else {
                update("favorites", FieldValue.arrayRemove(itemId))
            }
        }
    }

    suspend fun signOut() {
        auth.signOut()
        signInAnonymously()
    }

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    private fun usersReference(it: String) = firestore
        .collection("users")
        .document(it)

    private fun observeCurrentFirebaseUser() = callbackFlow {
        val listener: (FirebaseAuth) -> Unit = { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)

        awaitClose { auth.removeAuthStateListener(listener) }
    }

    private fun mapFirebaseUser(firebaseUser: FirebaseUser) = User(
        id = firebaseUser.uid,
        displayName = firebaseUser.displayName.orEmpty(),
        imageUrl = firebaseUser.photoUrl?.toString(),
        anonymous = firebaseUser.isAnonymous
    )
}

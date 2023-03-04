package com.kafka.data.feature.item.auth

import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.kafka.data.entities.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface AccountsRepository {
    suspend fun getCurrentUser(): User?
    fun observeCurrentUser(): Flow<User?>

    suspend fun signInAnonymously(): User?
    suspend fun signInUser(email: String, password: String): User?
    suspend fun signUpOrLinkUser(email: String, password: String): User?

    suspend fun signOut()
}

/**
 * Account management has complex rules specifically due to anonymous authentication.
 * The app lets users sign in anonymously and then link their account to an email/password.
 * Anonymous users can keep their data when they link their account.
 * */
@Singleton
class AccountRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    private val currentFirebaseUser
        get() = auth.currentUser

    private val usersCollection
        get() = firestore.collection("users")

    private fun getUserDocument(userId: String) = usersCollection.document(userId)
    private suspend fun getUserSnapshot(userId: String) = getUserDocument(userId).get().await()
    private suspend fun getUser(userId: String): User? =
        getUserSnapshot(userId)?.toObject(User::class.java)
    suspend fun getCurrentUser(): User? = currentFirebaseUser?.uid?.let { getUser(it) }

    private fun observeUser(userId: String): Flow<User?> =
        getUserDocument(userId).snapshots().map { it.toObject(User::class.java) }
    fun observeCurrentUser() = observeCurrentFirebaseUser()
        .flatMapLatest {
            if (it == null) {
                flowOf(null)
            } else {
                observeUser(it.uid)
            }
        }

    suspend fun signInAnonymously() =
        auth.signInAnonymously().await()?.user?.let { mapFirebaseUser(it) }

    suspend fun signUpOrLinkUser(email: String, password: String): User? {
        return if (currentFirebaseUser != null) {
            val credential = EmailAuthProvider.getCredential(email, password)
            val result = currentFirebaseUser?.linkWithCredential(credential)?.await()
            result?.user?.let { mapFirebaseUser(it, getUser(it.uid)) }
        } else {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            result.user?.let { mapFirebaseUser(it) }
        }
    }

    suspend fun signInUser(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await().user?.let { getUser(it.uid) }

    suspend fun updateUser(user: User) {
        getUserDocument(user.id).set(user).await()
    }

    suspend fun updateFavorite(itemId: String, isFavorite: Boolean) {
        currentFirebaseUser?.uid?.let { getUserDocument(it) }?.run {
            if (isFavorite) {
                update("favorites", FieldValue.arrayUnion(itemId))
            } else {
                update("favorites", FieldValue.arrayRemove(itemId))
            }
        }?.await()
    }

    fun signOut() {
        auth.signOut()
    }

    suspend fun resetPassword(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    private fun observeCurrentFirebaseUser() = callbackFlow {
        val listener: (FirebaseAuth) -> Unit = { firebaseAuth ->
            trySend(firebaseAuth.currentUser)
        }
        auth.addAuthStateListener(listener)

        awaitClose { auth.removeAuthStateListener(listener) }
    }

    private fun mapFirebaseUser(firebaseUser: FirebaseUser, existingUser: User? = null) = User(
        id = firebaseUser.uid,
        displayName = firebaseUser.displayName.orEmpty(),
        imageUrl = firebaseUser.photoUrl?.toString(),
        anonymous = firebaseUser.isAnonymous,
        favorites = existingUser?.favorites.orEmpty()
    )
}

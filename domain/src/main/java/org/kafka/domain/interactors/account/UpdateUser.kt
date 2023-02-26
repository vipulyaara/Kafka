package org.kafka.domain.interactors.account

import com.google.firebase.firestore.FirebaseFirestore
import com.kafka.data.entities.User
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UpdateUser @Inject constructor(private val firestore: FirebaseFirestore) {
    suspend fun execute(user: User) {
        val userDoc = firestore.collection("users").document(user.id)
        val favorites = userDoc.get().await().toObject(User::class.java)?.favorites.orEmpty()
        userDoc.set(user.copy(favorites = favorites)).await()
    }
}

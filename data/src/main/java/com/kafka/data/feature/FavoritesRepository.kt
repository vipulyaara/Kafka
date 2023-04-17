package com.kafka.data.feature

import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import dagger.Reusable
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@Reusable
class FavoritesRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository
) {
    fun observeRecentItems(uid: String) = firestoreGraph.getFavoritesCollection(uid)
        .snapshots()
        .map { snapshots ->
            snapshots.map { it.toObject<FavoriteItem>() }
        }

    suspend fun updateFavorite(favoriteItem: FavoriteItem, isFavorite: Boolean) {
        accountRepository.currentFirebaseUser?.uid
            ?.let { firestoreGraph.getFavoritesCollection(it) }
            ?.run {
                if (isFavorite) {
                    document(favoriteItem.itemId).set(favoriteItem)
                } else {
                    document(favoriteItem.itemId).delete()
                }
            }?.await()
    }
}

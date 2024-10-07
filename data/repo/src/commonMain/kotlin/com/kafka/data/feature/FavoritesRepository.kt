package com.kafka.data.feature

import com.google.firebase.firestore.Query
import com.kafka.base.ApplicationScope
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class FavoritesRepository @Inject constructor(
    private val firestoreGraph: FirestoreGraph,
    private val accountRepository: AccountRepository,
) {
    fun observeList(uid: String, listId: String) = firestoreGraph.getListCollection(uid, listId)
        .orderBy("createdAt", Query.Direction.DESCENDING)
        .snapshots()
        .map { snapshots -> snapshots.documents.map { it.data<FavoriteItem>() } }

    suspend fun updateList(favoriteItem: FavoriteItem, listId: String, addFavorite: Boolean) {
        accountRepository.currentFirebaseUser?.uid
            ?.let { firestoreGraph.getListCollection(it, listId) }
            ?.run {
                if (addFavorite) {
                    document(favoriteItem.itemId).set(favoriteItem)
                } else {
                    document(favoriteItem.itemId).delete()
                }
            }
    }
}

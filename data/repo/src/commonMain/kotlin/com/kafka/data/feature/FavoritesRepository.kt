package com.kafka.data.feature

import com.kafka.base.ApplicationScope
import com.kafka.data.dao.ItemDao
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.entities.Item
import com.kafka.data.feature.firestore.FirestoreGraph
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class FavoritesRepository @Inject constructor(
    private val itemDao: ItemDao,
    private val firestoreGraph: FirestoreGraph
) {
    fun observeFavorites(uid: String): Flow<List<Item>> {
        return firestoreGraph.favoriteListCollection(uid)
            .snapshots
            .map { querySnapshot ->
                querySnapshot.documents
                    .map { document -> document.data<FavoriteItem>() }
                    .map { itemDao.get(it.itemId) } //todo
            }
    }

//    fun observeList(uid: String, listId: String): Flow<List<Item>> {
//        return supabase.favoriteList
//            .safeSelectAsFlow(
//                primaryKeys = listOf(FavoriteItem::itemId, FavoriteItem::uid),
//                filter = FilterOperation("uid", FilterOperator.EQ, uid)
//            )
//            .map { it.sortedByDescending { it.createdAt } }
//            .map { favorites ->
//                val orderMap = favorites.withIndex().associate { it.value.itemId to it.index }
//                supabase.items.select {
//                    filter { Item::itemId isIn favorites.map { it.itemId } }
//                }.decodeList<Item>()
//                    .sortedWith(compareBy { orderMap[it.itemId] ?: Int.MAX_VALUE })
//            }
//    }

    suspend fun updateFavorite(uid: String, favoriteItem: FavoriteItem, addFavorite: Boolean) {
        if (addFavorite) {
            firestoreGraph.favoriteListCollection(uid)
                .document(favoriteItem.itemId)
                .set(favoriteItem)
        } else {
            firestoreGraph.favoriteListCollection(uid)
                .document(favoriteItem.itemId)
                .delete()
        }
    }

//    suspend fun updateList(favoriteItem: FavoriteItem, listId: String, addFavorite: Boolean) {
//        if (addFavorite) {
//            supabase.favoriteList.insert(favoriteItem)
//        } else {
//            supabase.favoriteList.delete {
//                filter { FavoriteItem::itemId eq favoriteItem.itemId }
//            }
//        }
//    }
}

@file:OptIn(SupabaseExperimental::class, SupabaseExperimental::class)

package com.kafka.data.feature

import com.kafka.base.ApplicationScope
import com.kafka.data.entities.FavoriteItem
import com.kafka.data.entities.Item
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.postgrest.query.filter.FilterOperation
import io.github.jan.supabase.postgrest.query.filter.FilterOperator
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class FavoritesRepository @Inject constructor(
    private val supabase: Supabase
) {
    fun observeList(uid: String, listId: String) = supabase.favoriteList
        .selectAsFlow(
            listOf(FavoriteItem::itemId, FavoriteItem::uid),
            filter = FilterOperation("uid", FilterOperator.EQ, uid)
        ).map { favorites ->
            supabase.items.select {
                filter { Item::itemId isIn favorites.map { it.itemId } }
            }.decodeList<Item>()
        }

    suspend fun updateList(favoriteItem: FavoriteItem, listId: String, addFavorite: Boolean) {
        if (addFavorite) {
            supabase.favoriteList.insert(favoriteItem)
        } else {
            supabase.favoriteList.delete {
                filter { FavoriteItem::itemId eq favoriteItem.itemId }
            }
        }
    }
}

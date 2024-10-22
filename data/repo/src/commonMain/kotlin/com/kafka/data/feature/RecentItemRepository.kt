@file:OptIn(ExperimentalCoroutinesApi::class, SupabaseExperimental::class)

package com.kafka.data.feature

import com.kafka.base.ApplicationScope
import com.kafka.data.entities.CurrentlyReading
import com.kafka.data.entities.Item
import com.kafka.data.entities.RecentItem
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.realtime.selectAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ApplicationScope
class RecentItemRepository @Inject constructor(
    private val supabase: Supabase
) {
    fun observeRecentItems(limit: Int) =
        supabase.recentItems.selectAsFlow(
            listOf(CurrentlyReading::fileId, CurrentlyReading::itemId, CurrentlyReading::uid)
        ).map {
            it.map { cr ->
                val it = supabase.books.select {
                    filter { Item::itemId eq cr.itemId }
                }.decodeSingle<Item>()

                RecentItem(
                    fileId = cr.fileId,
                    itemId = cr.itemId,
                    title = it.title,
                    coverUrl = it.coverImage.orEmpty(),
                    creator = it.creator,
                    updatedAt = System.currentTimeMillis(),
                    uid = cr.uid
                )
            }
        }
}

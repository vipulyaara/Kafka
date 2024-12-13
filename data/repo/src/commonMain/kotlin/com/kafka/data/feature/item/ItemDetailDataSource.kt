package com.kafka.data.feature.item

import com.kafka.base.CoroutineDispatchers
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.feature.Supabase
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class ItemDetailDataSource(
    private val dispatchers: CoroutineDispatchers,
    private val supabase: Supabase
) {
    suspend fun updateItemDetail(contentId: String): ItemDetail = withContext(dispatchers.io) {
        supabase.itemDetail.select {
            filter { ItemDetail::itemId eq contentId }
        }.decodeSingle<ItemDetail>()
    }

    suspend fun updateFiles(contentId: String): List<File> =
        withContext(dispatchers.io) {
            supabase.files.select {
                filter { File::itemId eq contentId }
            }.decodeList()
        }
}

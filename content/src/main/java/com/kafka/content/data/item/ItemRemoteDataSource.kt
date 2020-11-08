package com.kafka.content.data.item

import com.kafka.data.api.ArchiveService
import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.extensions.toResult
import com.kafka.data.model.model.Result
import com.kafka.data.entities.Item
import okhttp3.ResponseBody
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
class ItemRemoteDataSource @Inject constructor(
    private val archiveService: ArchiveService,
    private val itemMapper: ItemMapper
) {

    suspend fun fetchItemsByQuery(query: String): Result<List<Item>> {
        return archiveService
            .search(query)
            .executeWithRetry()
            .toResult(itemMapper)
    }

    suspend fun downloadFile(url: String): Result<ResponseBody> {
        return archiveService
            .downloadFileWithDynamicUrlSync(url)
            .executeWithRetry()
            .toResult { it }
    }

}

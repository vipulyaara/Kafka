package com.kafka.data.detail

import com.kafka.data.extensions.asyncOrAwait
import com.kafka.data.model.data.ErrorResult
import com.kafka.data.model.data.Success
import javax.inject.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ContentDetailRepository @Inject constructor(
    private val localDataSource: ContentDetailLocalDataSource,
    private val dataSource: ContentDetailDataSource
) {

    fun observeItemDetail(contentId: String) = localDataSource.itemDetailFlow(contentId)

    suspend fun updateContentDetail(contentId: String) {
        asyncOrAwait(key = "updateContentDetail") {
            val remote = dataSource.fetchItemDetail(contentId)

            if (remote is Success) {
                localDataSource.saveItemDetail(remote.data)
            }

            if (remote is ErrorResult) throw RuntimeException("Error Result")
        }
    }
}

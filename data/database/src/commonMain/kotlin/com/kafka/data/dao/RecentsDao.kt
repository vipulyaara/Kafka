package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.RecentAudioItem
import com.kafka.data.entities.RecentTextItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecentTextDao : EntityDao<RecentTextItem> {
    @Query("select * from recent_text where fileId = :fileId")
    abstract fun observe(fileId: String): Flow<RecentTextItem?>

    @Query("select * from recent_text where fileId = :fileId")
    abstract suspend fun getOrNull(fileId: String): RecentTextItem?

    @Query("update recent_text set currentPage = :currentPage where fileId = :fileId")
    abstract suspend fun updateCurrentPage(fileId: String, currentPage: Int)

    @Query("update recent_text set currentPageOffset = :currentPageOffset where fileId = :fileId")
    abstract suspend fun updateCurrentPageOffset(fileId: String, currentPageOffset: Int)

    suspend fun insertOrUpdateCurrentPage(fileId: String, currentPage: Int) {
        val recentTextItem = getOrNull(fileId)
        if (recentTextItem != null) {
            updateCurrentPage(fileId, currentPage)
        } else {
            insert(RecentTextItem(fileId, currentPage = currentPage))
        }
    }

    suspend fun insertOrUpdateCurrentPageOffset(fileId: String, currentPageOffset: Int) {
        val recentTextItem = getOrNull(fileId)
        if (recentTextItem != null) {
            updateCurrentPageOffset(fileId, currentPageOffset)
        } else {
            insert(RecentTextItem(fileId, currentPageOffset = currentPageOffset))
        }
    }
}

@Dao
abstract class RecentAudioDao : EntityDao<RecentAudioItem> {
    @Query("select * from recent_audio where albumId = :albumId")
    abstract suspend fun getByAlbumId(albumId: String): RecentAudioItem?

    @Query("select * from recent_audio where albumId = :albumId")
    abstract fun observeByAlbumId(albumId: String): Flow<RecentAudioItem?>

    @Query("update recent_audio set fileId = :fileId, currentTimestamp = :currentTimestamp where albumId = :albumId")
    abstract suspend fun updateNowPlaying(albumId: String, fileId: String, currentTimestamp: Long)
}

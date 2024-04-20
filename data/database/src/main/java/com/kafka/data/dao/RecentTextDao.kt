package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.RecentAudioItem
import com.kafka.data.entities.RecentTextItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class RecentTextDao : EntityDao<RecentTextItem> {
    @Query("select * from recent_text where fileId = :fileId")
    abstract fun observe(fileId: String): Flow<RecentTextItem>

    @Query("select * from recent_text where fileId = :fileId")
    abstract fun getOrNull(fileId: String): RecentTextItem?

    @Query("update recent_text set currentPage = :currentPage where fileId = :fileId")
    abstract suspend fun updateCurrentPage(fileId: String, currentPage: Int)
}

@Dao
abstract class RecentAudioDao : EntityDao<RecentAudioItem> {
    @Query("select * from recent_audio where fileId = :fileId")
    abstract suspend fun get(fileId: String): RecentAudioItem?

    @Query("select * from recent_audio where albumId = :albumId")
    abstract suspend fun getByAlbumId(albumId: String): RecentAudioItem?

    @Query("select * from recent_audio where fileId = :fileId")
    abstract fun observe(fileId: String): Flow<RecentAudioItem>

    @Query("select * from recent_audio where fileId IN (:fileIds)")
    abstract fun observe(fileIds: List<String>): Flow<List<RecentAudioItem>>

    @Query("update recent_audio set currentTimestamp = :timestamp where fileId = :fileId")
    abstract suspend fun updateTimestamp(fileId: String, timestamp: Long)
}

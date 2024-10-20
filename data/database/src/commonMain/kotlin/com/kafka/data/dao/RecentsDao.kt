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

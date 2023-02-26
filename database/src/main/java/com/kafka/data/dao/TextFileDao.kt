package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.RecentTextItem
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TextFileDao : EntityDao<RecentTextItem> {

    @Query("select * from  RecentTextItem where id = :fileId")
    abstract suspend fun get(fileId: String): RecentTextItem

    @Query("select * from RecentTextItem where id = :fileId")
    abstract fun observeFile(fileId: String): Flow<RecentTextItem>

    @Query("select * from RecentTextItem where itemId = :itemId ORDER BY title")
    abstract fun observeFilesByItemId(itemId: String): Flow<List<RecentTextItem>>

    @Query("select * from RecentTextItem where itemId = :itemId ORDER BY title")
    abstract suspend fun textByItemId(itemId: String): List<RecentTextItem>

    @Query("select * from RecentTextItem where id = :fileId")
    abstract fun entry(fileId: String): Flow<RecentTextItem>

    @Query("select * from RecentTextItem where itemId = :itemId")
    abstract fun entryByItemId(itemId: String): Flow<RecentTextItem>

    @Query("UPDATE RecentTextItem SET currentPage=:page where id = :fileId")
    abstract fun updateCurrentPage(fileId: String, page: Int)
}

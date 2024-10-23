package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kafka.data.entities.Download
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DownloadDao {
    @Query("SELECT * FROM Download WHERE fileId = :fileId")
    abstract suspend fun get(fileId: String): Download?

    @Query("SELECT * FROM Download WHERE fileId = :fileId")
    abstract fun observe(fileId: String): Flow<Download?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(download: Download)

    @Query("UPDATE Download SET status = :status WHERE fileId = :fileId")
    abstract suspend fun updateStatus(fileId: String, status: Download.Status)

    @Query("UPDATE Download SET progress = :progress WHERE fileId = :fileId")
    abstract suspend fun updateProgress(fileId: String, progress: Int)

    @Query("UPDATE Download SET filePath = :filePath WHERE fileId = :fileId")
    abstract suspend fun updateFilePath(fileId: String, filePath: String?)

    @Query("DELETE FROM Download WHERE fileId = :fileId")
    abstract suspend fun delete(fileId: String)
}

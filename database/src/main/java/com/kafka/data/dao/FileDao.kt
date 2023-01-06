package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kafka.data.entities.File
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class FileDao : EntityDao<File> {

    @Query("select * from File where localUri is not null")
    abstract fun observeDownloadedFiles(): Flow<List<File>>

    @Query("select * from File where fileId = :fileId")
    abstract fun observeFile(fileId: String): Flow<File>

    @Query("select * from File where itemId = :itemId")
    abstract fun observeFilesByItemId(itemId: String): Flow<List<File>>

    @Query("select * from File where itemId = :itemId")
    abstract suspend fun filesByItemId(itemId: String): List<File>

    @Query("select * from File where fileId = :fileId")
    abstract suspend fun file(fileId: String): File

    @Query("select * from File where fileId = :fileId")
    abstract suspend fun fileOrNull(fileId: String): File?

    @Transaction
    @Query("SELECT * FROM file WHERE fileId IN (:ids)")
    abstract suspend fun filesByIds(ids: List<String>): List<File>
}

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

    @Query("select * from File where fileId = :fileId")
    abstract fun observe(fileId: String): Flow<File?>

    @Query("select * from File where itemId = :itemId ORDER BY name")
    abstract fun observeByItemId(itemId: String): Flow<List<File>>

    @Query("select * from File where itemId = :itemId ORDER BY name")
    abstract suspend fun getByItemId(itemId: String): List<File>

    suspend fun playerFilesByItemId(itemId: String): List<File> {
        return getByItemId(itemId)
            .groupBy { it.format }
            .filter { it.key.contains("mp3", true) }
            .filterKeys { it.contains("mp3", true) }
            .values.flatten()
    }

    @Query("select * from File where fileId = :fileId")
    abstract fun entry(fileId: String): Flow<File>

    @Query("select * from File where fileId = :fileId")
    abstract suspend fun getOrNull(fileId: String): File?

    @Transaction
    @Query("SELECT * FROM file WHERE fileId IN (:ids) ORDER BY name")
    abstract suspend fun getByIds(ids: List<String>): List<File>

    @Transaction
    @Query("SELECT * FROM file WHERE name LIKE :title ORDER BY name")
    abstract fun entriesByTitle(title: String): Flow<List<File>>
}

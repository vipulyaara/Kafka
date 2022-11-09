package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.File
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class FileDao : EntityDao<File> {

    @Query("select * from File where fileId = :fileId")
    abstract fun observeFile(fileId: String): Flow<File>

    @Query("select * from File where itemId = :itemId")
    abstract fun observeFilesByItemId(itemId: String): Flow<List<File>>

    @Query("select * from File where itemId = :itemId")
    abstract fun filesByItemId(itemId: String): List<File>
}

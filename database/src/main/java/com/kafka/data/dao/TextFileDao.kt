package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.TextFile
import kotlinx.coroutines.flow.Flow

@Dao
abstract class TextFileDao : EntityDao<TextFile> {

    @Query("select * from  TextFile where id = :fileId")
    abstract suspend fun get(fileId: String): TextFile

    @Query("select * from TextFile where id = :fileId")
    abstract fun observeFile(fileId: String): Flow<TextFile>

    @Query("select * from TextFile where itemId = :itemId ORDER BY title")
    abstract fun observeFilesByItemId(itemId: String): Flow<List<TextFile>>

    @Query("select * from TextFile where itemId = :itemId ORDER BY title")
    abstract suspend fun textByItemId(itemId: String): List<TextFile>

    @Query("select * from TextFile where id = :fileId")
    abstract fun entry(fileId: String): Flow<TextFile>

    @Query("select * from TextFile where itemId = :itemId")
    abstract fun entryByItemId(itemId: String): Flow<TextFile>

    @Query("UPDATE TextFile SET currentPage=:page where id = :fileId")
    abstract fun updateCurrentPage(fileId: String, page: Int)
}

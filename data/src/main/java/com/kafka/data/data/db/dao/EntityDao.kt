package com.kafka.data.data.db.dao

import androidx.room.*
import com.kafka.data.entities.BaseEntity

interface EntityDao<in E : BaseEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: E): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<E>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: E)

    @Delete
    suspend fun delete(entity: E): Int
}

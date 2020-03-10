package com.kafka.data.data.db.dao

import androidx.room.*
import com.kafka.data.model.common.BaseEntity

abstract class EntityDao<in E : BaseEntity> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(entity: E): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun  insertAll(vararg entity: E)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun  insertAll(entities: List<E>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun  update(entity: E)

    @Delete
    abstract suspend fun  delete(entity: E): Int

    @Transaction
    open suspend fun withTransaction(tx: suspend () -> Unit) = tx()
}

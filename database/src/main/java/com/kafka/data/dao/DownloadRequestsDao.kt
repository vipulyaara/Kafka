/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.kafka.data.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kafka.data.entities.DownloadRequest
import kotlinx.coroutines.flow.Flow

@Dao
abstract class DownloadRequestsDao : EntityDao<DownloadRequest> {

    @Transaction
    @Query("SELECT * FROM download_requests WHERE id in (:ids) ORDER BY created_at DESC, id ASC")
    abstract suspend fun getByIds(ids: List<String>): List<DownloadRequest>

    @Transaction
    @Query("SELECT * FROM download_requests WHERE id == :id ORDER BY created_at DESC, id ASC")
    abstract suspend fun getById(id: List<String>): DownloadRequest

    @Transaction
    @Query("SELECT * FROM download_requests WHERE request_id == :id ORDER BY created_at DESC, id ASC")
    abstract suspend fun getByRequestId(id: Int): DownloadRequest

    @Transaction
    @Query("SELECT * FROM download_requests ORDER BY created_at DESC, id ASC")
    abstract fun entries(): Flow<List<DownloadRequest>>

    @Transaction
    @Query("SELECT * FROM download_requests ORDER BY created_at DESC, id ASC LIMIT :count OFFSET :offset")
    abstract fun entries(count: Int, offset: Int): Flow<List<DownloadRequest>>

    @Transaction
    @Query("SELECT * FROM download_requests ORDER BY created_at DESC, id ASC")
    abstract fun entriesPagingSource(): PagingSource<Int, DownloadRequest>

    @Transaction
    @Query("SELECT * FROM download_requests WHERE id = :id")
    abstract fun entry(id: String): Flow<DownloadRequest>

    @Transaction
    @Query("SELECT * FROM download_requests WHERE id = :id")
    abstract fun entryNullable(id: String): Flow<DownloadRequest?>

    @Transaction
    @Query("SELECT * FROM download_requests WHERE id in (:ids)")
    abstract fun entriesById(ids: List<String>): Flow<List<DownloadRequest>>

    @Query("DELETE FROM download_requests WHERE id = :id")
    abstract suspend fun delete(id: String): Int

    @Query("DELETE FROM download_requests")
    abstract suspend fun deleteAll(): Int

    @Query("SELECT COUNT(*) from download_requests")
    abstract suspend fun count(): Int

    @Query("SELECT COUNT(*) from download_requests")
    abstract fun observeCount(): Flow<Int>

    @Query("SELECT COUNT(*) from download_requests where id = :id")
    abstract fun has(id: String): Flow<Int>

    @Query("SELECT COUNT(*) from download_requests where id = :id")
    abstract suspend fun exists(id: String): Int
}

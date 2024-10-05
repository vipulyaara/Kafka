/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package com.kafka.data.dao

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
    @Query("SELECT * FROM download_requests WHERE request_id == :id ORDER BY created_at DESC, id ASC")
    abstract suspend fun getByRequestIdOrNull(id: Int): DownloadRequest?

    @Transaction
    @Query("SELECT * FROM download_requests ORDER BY created_at DESC, id ASC")
    abstract fun entries(): Flow<List<DownloadRequest>>

    @Transaction
    @Query("SELECT * FROM download_requests ORDER BY created_at DESC, id ASC LIMIT :count OFFSET :offset")
    abstract fun entries(count: Int, offset: Int): Flow<List<DownloadRequest>>

    @Transaction
    @Query("SELECT * FROM download_requests WHERE id = :id")
    abstract fun entry(id: String): Flow<DownloadRequest>

    @Query("DELETE FROM download_requests WHERE id = :id")
    abstract suspend fun delete(id: String): Int

    @Query("SELECT COUNT(*) from download_requests where id = :id")
    abstract suspend fun exists(id: String): Int
}

package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.kafka.data.entities.Audio
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

@Dao
abstract class AudioDao : EntityDao<Audio> {

    @Query("DELETE FROM audio")
    abstract suspend fun deleteAll(): Int

    @Transaction
    @Query("SELECT * FROM audio WHERE id IN (:ids)")
    abstract fun entriesById(ids: List<String>): Flow<List<Audio>>

    @Transaction
    @Query("SELECT * FROM audio")
    abstract fun entries(): Flow<List<Audio>>

    @Transaction
    @Query("SELECT * FROM audio WHERE id = :id")
    abstract fun entry(id: String): Flow<Audio>

    suspend fun findAudios(ids: List<String>): List<Audio> {
        val audios = entriesById(ids.toList()).firstOrNull().orEmpty().map { it.id to it }.toMap()
        return buildList {
            ids.forEach { id ->
                val audio = audios[id]
                if (audio == null) {
                    Timber.e("Couldn't find audio by id: $id")
                } else add(audio)
            }
        }
    }

    suspend fun findAudio(id: String): Audio? = findAudios(listOf(id)).firstOrNull()
}

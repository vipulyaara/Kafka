package com.kafka.data.dao

import androidx.room.*
import com.kafka.data.entities.QueueEntity
import com.kafka.data.entities.Song
import kotlinx.coroutines.flow.Flow

@Dao
interface QueueDao : EntityDao<Song> {
    @Query("SELECT * FROM song order by title")
    fun observeQueueSongs(): Flow<List<Song>>

    @Query("SELECT * FROM song order by title")
    suspend fun getQueueSongs(): List<Song?>

    @Query("SELECT * FROM song where id = :id")
    suspend fun getSongById(id: String): Song

    @Query("SELECT currentSeekPos FROM queue_meta_data")
    fun observeSeekPosition(): Flow<Long?>

    @Query("SELECT isPlaying FROM queue_meta_data")
    fun observePlayingStatus(): Flow<Boolean?>

    @Query("SELECT * FROM queue_meta_data")
    fun observePlayingQueue(): Flow<QueueEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQueueEntity(queueEntity: QueueEntity)

    @Query("SELECT * FROM queue_meta_data")
    suspend fun getQueueEntity(): QueueEntity?

    @Query("SELECT currentSongId FROM queue_meta_data")
    fun observeCurrentSongId(): Flow<String?>

    @Query("UPDATE queue_meta_data SET currentSongId  = :currentSongId")
    suspend fun updateCurrentSong(currentSongId: String)

    @Query("UPDATE queue_meta_data SET isPlaying = :isPlaying")
    suspend fun updatePlayingStatus(isPlaying: Boolean)

    @Query("UPDATE queue_meta_data SET currentSeekPos = :seek")
    suspend fun updatePlayerSeekPosition(seek: Long)

    @Query("DELETE from song")
    fun clearSongs()

    @Delete
    fun delete(song: Song)
}

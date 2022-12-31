package com.kafka.user.injection

import com.kafka.data.dao.AudioDao
import com.kafka.data.entities.Audio
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.apis.PlayerEventLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.sarahang.playback.core.models.Audio as PlayerAudio

@InstallIn(SingletonComponent::class)
@Module
class PlayerModule {

    @Provides
    fun audioDataSource(audioDao: AudioDao): AudioDataSource = object : AudioDataSource {
        override suspend fun getByMediaIds(mediaIds: List<String>): List<PlayerAudio> {
            return audioDao.findAudios(mediaIds).map { it.asPlayerAudio() }
        }

        override suspend fun getByIds(ids: List<String>): List<PlayerAudio> {
            return audioDao.findAudios(ids).map { it.asPlayerAudio() }
        }

        override suspend fun findAudio(id: String): PlayerAudio? {
            return audioDao.findAudio(id)?.asPlayerAudio()
        }

        override suspend fun findAudioList(id: String): List<PlayerAudio> {
            return listOf(audioDao.findAudio(id)).map { it?.asPlayerAudio() ?: PlayerAudio.unknown }
        }
    }

    @Provides
    fun audioLogger(): PlayerEventLogger = object : PlayerEventLogger {

    }
}

fun Audio.asPlayerAudio() = PlayerAudio(
    id = id,
    title = title,
    artist = artistId,
    album = creator,
    playbackUrl = playbackUrl,
    duration = duration,
    coverImage = coverImage
)

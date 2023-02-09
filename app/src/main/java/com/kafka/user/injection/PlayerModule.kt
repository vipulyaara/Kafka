package com.kafka.user.injection

import com.kafka.data.dao.FileDao
import com.kafka.data.entities.isPlayable
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.apis.PlayerEventLogger
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.kafka.item.files.asAudio
import com.sarahang.playback.core.models.Audio as PlayerAudio

@InstallIn(SingletonComponent::class)
@Module
class PlayerModule {

    @Provides
    fun audioDataSource(fileDao: FileDao): AudioDataSource = object : AudioDataSource {
        override suspend fun getByIds(ids: List<String>): List<PlayerAudio> {
            return fileDao.filesByIds(ids)
                .sortedBy { it.format }
                .map { it.asAudio() }
        }

        override suspend fun findAudio(id: String): PlayerAudio? {
            return fileDao.fileOrNull(id)?.asAudio()
        }

        override suspend fun findAudioList(id: String): List<PlayerAudio> {
            return listOf(fileDao.fileOrNull(id)).map { it?.asAudio() ?: PlayerAudio.unknown }
        }

        override suspend fun findAudiosByItemId(itemId: String): List<PlayerAudio> {
            return fileDao.playerFilesByItemId(itemId)
                .filter { it.isPlayable() }
                .distinctBy { it.title }
                .map { it.asAudio() }
        }
    }

    @Provides
    fun audioLogger(): PlayerEventLogger = object : PlayerEventLogger {

    }
}

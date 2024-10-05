package com.kafka.user.playback

import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File
import com.kafka.data.entities.isPlayable
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.models.Audio
import javax.inject.Inject

class PlayerAudioDataSource @Inject constructor(private val fileDao: FileDao) : AudioDataSource {
    override suspend fun getByIds(ids: List<String>): List<Audio> {
        return fileDao.getByIds(ids.take(SQL_QUERY_LIMIT))
            .sortedBy { it.format }
            .map { it.asAudio() }
    }

    override suspend fun findAudio(id: String): Audio? {
        return fileDao.getOrNull(id)?.asAudio()
    }

    override suspend fun findAudioList(id: String): List<Audio> {
        return listOf(fileDao.getOrNull(id)).map { it?.asAudio() ?: Audio.unknown }
    }

    override suspend fun findAudiosByItemId(itemId: String): List<Audio> {
        return fileDao.playerFilesByItemId(itemId)
            .filter { it.isPlayable() }
            .distinctBy { it.title }
            .map { it.asAudio() }
    }
}

const val SQL_QUERY_LIMIT = 990

fun File.asAudio() = Audio(
    id = fileId,
    title = title,
    artist = creator,
    album = itemTitle,
    albumId = itemId,
    duration = duration,
    playbackUrl = playbackUrl.orEmpty(),
    localUri = localUri,
    coverImage = coverImage
)

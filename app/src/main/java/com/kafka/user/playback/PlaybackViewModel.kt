package com.kafka.user.playback

import androidx.lifecycle.ViewModel
import com.kafka.data.dao.FileDao
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.id
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val fileDao: FileDao
) : ViewModel() {

    suspend fun getCurrentItemId(): String {
        val nowPlaying = playbackConnection.nowPlaying.value
        return withContext(Dispatchers.IO) {
            fileDao.filesByItemId(nowPlaying.id!!).first().itemId
        }
    }
}

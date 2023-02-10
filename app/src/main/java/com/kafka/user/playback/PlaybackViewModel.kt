package com.kafka.user.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.dao.FileDao
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.album
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kafka.navigation.LeafScreen
import org.kafka.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class PlaybackViewModel @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val fileDao: FileDao,
    private val navigator: Navigator
) : ViewModel() {

    fun goToAlbum() {
        viewModelScope.launch {
            val currentRoot = navigator.currentRoot.value
            getCurrentItemId()?.let {
                navigator.navigate(LeafScreen.ItemDetail.buildRoute(id = it, root = currentRoot))
            }
        }
    }

    private suspend fun getCurrentItemId(): String? {
        val nowPlaying = playbackConnection.nowPlaying.value
        return withContext(Dispatchers.IO) {
            nowPlaying.album?.let { fileDao.filesByItemId(it).firstOrNull()?.itemId }
        }
    }
}

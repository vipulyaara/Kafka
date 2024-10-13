package com.kafka.shared.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.dao.FileDao
import com.kafka.data.model.SearchFilter
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import com.kafka.navigation.graph.Screen.Search
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getPlayerTheme
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.models.toMediaId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class PlaybackViewModel @Inject constructor(
    private val playbackConnection: PlaybackConnection,
    private val fileDao: FileDao,
    private val navigator: Navigator,
    private val remoteConfig: RemoteConfig,
) : ViewModel() {
    val playerTheme by lazy { remoteConfig.getPlayerTheme() }

    fun goToAlbum() {
        viewModelScope.launch(Dispatchers.IO) {
            playbackConnection.nowPlaying.value.id.toMediaId().value.let { id ->
                fileDao.getOrNull(id)!!.let { file ->
                    navigator.navigate(Screen.ItemDetail(file.itemId))
                }
            }
        }
    }

    fun goToCreator() {
        viewModelScope.launch {
            val artist = playbackConnection.nowPlaying.value.artist.orEmpty()
            navigator.navigate(
                route = Search(keyword = artist, filters = SearchFilter.Creator.name),
                root = RootScreen.Search
            )
        }
    }
}

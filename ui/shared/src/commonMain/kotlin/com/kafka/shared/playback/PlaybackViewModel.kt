package com.kafka.shared.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.data.dao.FileDao
import com.kafka.navigation.Navigator
import com.kafka.navigation.graph.RootScreen
import com.kafka.navigation.graph.Screen
import com.kafka.navigation.graph.Screen.Search
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.getPlayerTheme
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.models.toMediaId
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class PlaybackViewModel(
    private val playbackConnection: PlaybackConnection,
    private val fileDao: FileDao,
    private val navigator: Navigator,
    private val remoteConfig: RemoteConfig,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {
    val playerTheme by lazy { remoteConfig.getPlayerTheme() }

    fun goToAlbum() {
        viewModelScope.launch(dispatchers.io) {
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
                route = Search(keyword = artist),
                root = RootScreen.Search
            )
        }
    }
}

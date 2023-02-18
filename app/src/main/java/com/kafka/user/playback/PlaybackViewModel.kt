package com.kafka.user.playback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.data.dao.FileDao
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.artist
import com.sarahang.playback.core.id
import com.sarahang.playback.core.models.toMediaId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
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
        viewModelScope.launch(Dispatchers.IO) {
            val currentRoot = navigator.currentRoot.value
            val nowPlaying = playbackConnection.nowPlaying.value

            nowPlaying.id.toMediaId().value.let { id ->
                fileDao.getOrNull(id)!!.let { file ->
                    navigator.navigate(
                        LeafScreen.ItemDetail.buildRoute(
                            id = file.itemId,
                            root = currentRoot
                        )
                    )
                }
            }
        }
    }

    fun goToCreator() {
        viewModelScope.launch {
            val currentRoot = navigator.currentRoot.value
            val creator = playbackConnection.nowPlaying.value.artist
            navigator.navigate(LeafScreen.Search.buildRoute(creator, currentRoot))
        }
    }
}

package com.kafka.player.timber.playback

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.kafka.data.extensions.debug
import com.kafka.data.CustomScope
import com.kafka.data.dao.QueueDao
import com.kafka.player.playback.player.Player
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : Service(), LifecycleOwner, CoroutineScope by CustomScope() {

    @Inject lateinit var player: Player
    @Inject lateinit var queueDao: QueueDao
    private val lifecycle = LifecycleRegistry(this)

    override fun getLifecycle() = lifecycle

    override fun onCreate() {
        super.onCreate()
        lifecycle.currentState = Lifecycle.State.RESUMED

        debug { "Music service created" }

        player.start()

        launch(IO) {
            player.setQueue(queueDao.getQueueSongs().filterNotNull())
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        lifecycle.currentState = Lifecycle.State.DESTROYED
        debug { "onDestroy()" }
        player.release()
        super.onDestroy()
    }
}

package com.kafka.player.timber.playback

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.kafka.data.CustomScope
import com.kafka.data.dao.QueueDao
import com.kafka.player.domain.ObserveCurrentSong
import com.kafka.player.domain.ObserveQueueSongs
import com.kafka.player.playback.notification.NotificationHandler
import com.kafka.player.playback.player.Player
import com.kafka.player.timber.permissions.PermissionsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import timber.log.Timber.d as log

@AndroidEntryPoint
class MusicService : Service(), LifecycleOwner, CoroutineScope by CustomScope() {

    @Inject lateinit var notificationHandler: NotificationHandler
    @Inject lateinit var player: Player
    @Inject lateinit var queueDao: QueueDao
    @Inject lateinit var observeCurrentSong: ObserveCurrentSong
    @Inject lateinit var observeQueueSongs: ObserveQueueSongs
    @Inject lateinit var permissionsManager: PermissionsManager

    private lateinit var becomingNoisyReceiver: BecomingNoisyReceiver
    private val lifecycle = LifecycleRegistry(this)

    override fun getLifecycle() = lifecycle

    override fun onCreate() {
        super.onCreate()
        lifecycle.currentState = Lifecycle.State.RESUMED

        launch(IO) {
            permissionsManager.requestStoragePermission(waitForGranted = true)
                .collect { player.setQueue(queueDao.getQueueSongs().filterNotNull()) }

            observeCurrentSong.observe().collect {
                notificationHandler.updateNotification()
            }
            observeCurrentSong(Unit)
        }

//        becomingNoisyReceiver = BecomingNoisyReceiver(this, MediaSessionCompat.Token(5))

//        GlobalScope.launch {
//            player.addStateChangeListener {
//                if (it.isPlaying) {
//                    becomingNoisyReceiver.register()
//                } else {
//                    becomingNoisyReceiver.unregister()
//                    saveCurrentData()
//                }
//                notificationHandler.updateNotification(player.cu)
//            }
//        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        lifecycle.currentState = Lifecycle.State.DESTROYED
        log("onDestroy()")
        player.release()
        super.onDestroy()
    }
}

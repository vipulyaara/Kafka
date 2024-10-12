package com.kafka.user

import android.app.Application
import com.kafka.user.injection.AndroidApplicationComponent
import com.kafka.user.injection.create
import com.sarahang.playback.core.MediaNotifications
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.apis.Logger
import com.sarahang.playback.core.players.MediaSessionPlayer
import com.sarahang.playback.core.players.SarahangPlayer
import com.sarahang.playback.core.services.PlayerServiceDependencies
import com.sarahang.playback.core.timer.SleepTimer

/**
 * @author Vipul Kumar; dated 21/12/18.
 */
class KafkaApplication : Application(), PlayerServiceDependencies {
    internal val component: AndroidApplicationComponent by lazy(LazyThreadSafetyMode.NONE) {
        AndroidApplicationComponent::class.create(this)
    }

    override fun onCreate() {
        super.onCreate()

        component.appInitializers.forEach { it.init() }
    }

    override val player: SarahangPlayer
        get() = component.player
    override val timer: SleepTimer
        get() = component.timer
    override val logger: Logger
        get() = component.logger
    override val mediaNotifications: MediaNotifications
        get() = component.mediaNotifications
    override val audioDataSource: AudioDataSource
        get() = component.audioDataSource
    override val playbackConnection: PlaybackConnection
        get() = component.playbackConnection
    override val sessionPlayer: MediaSessionPlayer
        get() = component.sessionPlayer
}

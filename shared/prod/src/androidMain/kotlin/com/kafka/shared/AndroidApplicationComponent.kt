package com.kafka.shared

import android.app.Application
import com.kafka.base.ApplicationScope
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.ProcessLifetime
import com.kafka.data.prefs.PreferencesStore
import com.kafka.remote.config.RemoteConfig
import com.kafka.shared.common.initializer.AppInitializers
import com.kafka.shared.common.injection.SharedApplicationComponent
import com.sarahang.playback.core.MediaNotifications
import com.sarahang.playback.core.PlaybackConnection
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.apis.Logger
import com.sarahang.playback.core.players.MediaSessionPlayer
import com.sarahang.playback.core.players.SarahangPlayer
import com.sarahang.playback.core.timer.SleepTimer
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides

@Component
@ApplicationScope
abstract class AndroidApplicationComponent(
    @get:Provides val application: Application,
): SharedApplicationComponent {
    abstract val appInitializers: AppInitializers
    abstract val preferencesStore: PreferencesStore
    abstract val remoteConfig: RemoteConfig
    abstract val dispatchers: CoroutineDispatchers
    @ProcessLifetime
    abstract val processScope: CoroutineScope

    abstract val player: SarahangPlayer
    abstract val sessionPlayer: MediaSessionPlayer
    abstract val timer: SleepTimer
    abstract val logger: Logger
    abstract val mediaNotifications: MediaNotifications
    abstract val audioDataSource: AudioDataSource
    abstract val playbackConnection: PlaybackConnection

    companion object
}

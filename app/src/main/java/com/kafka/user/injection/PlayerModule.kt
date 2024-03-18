package com.kafka.user.injection

import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.isExactAlarmEnabled
import com.kafka.user.playback.KafkaPlayerEventLogger
import com.kafka.user.playback.PlayerAudioDataSource
import com.sarahang.playback.core.PlayerRemoteConfig
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.apis.PlayerEventLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@InstallIn(SingletonComponent::class)
@Module
abstract class PlayerModule {

    @Binds
    abstract fun audioDataSource(playerAudioDataSource: PlayerAudioDataSource): AudioDataSource

    @Binds
    abstract fun playerEventLogger(kafkaPlayerEventLogger: KafkaPlayerEventLogger): PlayerEventLogger

    @Binds
    abstract fun PlayerRemoteConfig(playerRemoteConfig: PlayerRemoteConfigImpl): PlayerRemoteConfig
}

class PlayerRemoteConfigImpl @Inject constructor(
    private val remoteConfig: RemoteConfig
) : PlayerRemoteConfig {
    override fun isExactAlarmEnabled(): Boolean {
        return remoteConfig.isExactAlarmEnabled()
    }
}

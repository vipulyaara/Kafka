package com.kafka.user.injection

import com.kafka.user.playback.KafkaPlayerEventLogger
import com.kafka.user.playback.PlayerAudioDataSource
import com.kafka.user.playback.PlayerLogger
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.apis.Logger
import com.sarahang.playback.core.apis.PlayerEventLogger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class PlayerModule {

    @Binds
    abstract fun audioDataSource(playerAudioDataSource: PlayerAudioDataSource): AudioDataSource

    @Binds
    abstract fun playerEventLogger(kafkaPlayerEventLogger: KafkaPlayerEventLogger): PlayerEventLogger

    @Binds
    abstract fun playerLogger(playerLogger: PlayerLogger): Logger
}

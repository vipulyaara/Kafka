package com.kafka.user.injection

import com.kafka.user.playback.KafkaPlayerEventLogger
import com.kafka.user.playback.PlayerAudioDataSource
import com.kafka.user.playback.PlayerLogger
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.apis.Logger
import com.sarahang.playback.core.apis.PlayerEventLogger
import com.sarahang.playback.core.injection.PlaybackCoreModule
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import org.kafka.base.ApplicationScope

@Component
@ApplicationScope
interface PlayerModule : PlaybackCoreModule {

    @Provides
    fun audioDataSource(bind: PlayerAudioDataSource): AudioDataSource = bind

    @Provides
    fun playerEventLogger(bind: KafkaPlayerEventLogger): PlayerEventLogger = bind

    @Provides
    fun playerLogger(bind: PlayerLogger): Logger = bind
}

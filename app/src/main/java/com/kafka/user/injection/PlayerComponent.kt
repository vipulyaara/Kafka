package com.kafka.user.injection

import com.kafka.user.playback.KafkaPlayerEventLogger
import com.kafka.user.playback.PlayerAudioDataSource
import com.kafka.user.playback.PlayerLogger
import com.sarahang.playback.core.apis.AudioDataSource
import com.sarahang.playback.core.apis.Logger
import com.sarahang.playback.core.apis.PlayerEventLogger
import com.sarahang.playback.core.injection.PlaybackCoreComponent
import me.tatarka.inject.annotations.Provides
import com.kafka.base.ApplicationScope

@ApplicationScope
interface PlayerComponent : PlaybackCoreComponent {

    @Provides
    @ApplicationScope
    fun audioDataSource(bind: PlayerAudioDataSource): AudioDataSource = bind

    @Provides
    @ApplicationScope
    fun playerEventLogger(bind: KafkaPlayerEventLogger): PlayerEventLogger = bind

    @Provides
    @ApplicationScope
    fun playerLogger(bind: PlayerLogger): Logger = bind
}

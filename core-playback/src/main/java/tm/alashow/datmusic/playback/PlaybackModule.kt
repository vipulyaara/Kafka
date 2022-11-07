/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.playback

import android.content.ComponentName
import android.content.Context
import com.kafka.data.dao.AudioDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.kafka.analytics.LogContentEvent
import tm.alashow.datmusic.playback.players.AudioPlayerImpl
import tm.alashow.datmusic.playback.services.PlayerService
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class PlaybackModule {

    @Provides
    @Singleton
    fun playbackConnection(
        @ApplicationContext context: Context,
        audiosDao: AudioDao,
        logContentEvent: LogContentEvent,
        audioPlayer: AudioPlayerImpl
    ): PlaybackConnection = PlaybackConnectionImpl(
        context = context,
        serviceComponent = ComponentName(context, PlayerService::class.java),
        audiosDao = audiosDao,
        audioPlayer = audioPlayer,
        logContentEvent = logContentEvent
    )
}

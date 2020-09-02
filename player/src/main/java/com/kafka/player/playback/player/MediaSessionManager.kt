package com.kafka.player.playback.player

import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaSessionManager @Inject constructor(@ApplicationContext private val context: Context) {
    val mediaSession by lazy {
        MediaSessionCompat(context, context.packageName).apply {
            setPlaybackState(createDefaultPlaybackState().build())
        }
    }

    private val mediaSessionConnector by lazy { MediaSessionConnector(mediaSession) }

    fun connect(player: Player) {
        mediaSessionConnector.setPlayer(player, null)
    }

    private fun createDefaultPlaybackState(): PlaybackStateCompat.Builder {
        return PlaybackStateCompat.Builder().setActions(
            PlaybackStateCompat.ACTION_PLAY
                    or PlaybackStateCompat.ACTION_PAUSE
                    or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                    or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                    or PlaybackStateCompat.ACTION_PLAY_PAUSE
                    or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
                    or PlaybackStateCompat.ACTION_SET_REPEAT_MODE
        ).setState(PlaybackStateCompat.STATE_NONE, 0, 1f)
    }
}

package com.kafka.player.playback

import android.app.PendingIntent
import android.graphics.Bitmap
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.kafka.player.playback.model.MediaItem
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DescriptionAdapter @Inject constructor() : PlayerNotificationManager.MediaDescriptionAdapter {
    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return null
    }

    override fun getCurrentContentText(player: Player): CharSequence? {
        return (player.currentTag as MediaItem).subtitle
    }

    override fun getCurrentContentTitle(player: Player): CharSequence {
        return (player.currentTag as MediaItem).title
    }

    override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
        GlobalScope.launch {

        }
        return null
    }

}

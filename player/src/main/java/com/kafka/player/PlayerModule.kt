package com.kafka.player

import android.app.Application
import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import com.kafka.player.playback.Player
import com.kafka.player.playback.PlayerLifecycle
import com.kafka.player.playback.RealPlayer
import com.kafka.player.timber.db.QueueHelper
import com.kafka.player.timber.db.RealQueueHelper
import com.kafka.player.timber.notifications.Notifications
import com.kafka.player.timber.notifications.RealNotifications
import com.kafka.player.timber.permissions.PermissionsManager
import com.kafka.player.timber.permissions.RealPermissionsManager
import com.kafka.player.timber.playback.MediaSessionConnection
import com.kafka.player.timber.playback.RealMediaSessionConnection
import com.kafka.player.timber.playback.TimberMusicService
import com.kafka.player.timber.playback.players.*
import com.kafka.player.timber.repository.PlaylistRepository
import com.kafka.player.timber.repository.RealPlaylistRepository
import com.kafka.player.timber.repository.RealSongsRepository
import com.kafka.player.timber.repository.SongsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class MediaModuleBinds {
    @Binds
    abstract fun bindPlayerLifecycle(realPlayer: RealPlayer): PlayerLifecycle

    @Binds
    abstract fun bindPlayer(realPlayer: RealPlayer): Player

    @Binds
    abstract fun bindMusicPlayer(realMusicPlayer: RealMusicPlayer): MusicPlayer

    @Binds
    abstract fun bindQueue(realQueue: RealQueue): Queue

    @Binds
    abstract fun bindSongPlayer(realSongPlayer: RealSongPlayer): SongPlayer

    @Binds
    abstract fun bindPermissionsManager(realPermissionsManager: RealPermissionsManager): PermissionsManager

    @Binds
    abstract fun bindNotifications(realNotifications: RealNotifications): Notifications

    @Binds
    abstract fun bindSongsRepository(realSongsRepository: RealSongsRepository): SongsRepository

    @Binds
    abstract fun bindMediaSessionConnection(realMediaSessionConnection: RealMediaSessionConnection): MediaSessionConnection

    @Binds
    abstract fun bindPlaylistRepository(realPlaylistRepository: RealPlaylistRepository): PlaylistRepository

    @Binds
    abstract fun bindQueueHelper(realQueueHelper: RealQueueHelper): QueueHelper
}

@Module
@InstallIn(ApplicationComponent::class)
class MediaModule {
    @Provides
    fun providesNotificationService(application: Application): NotificationManager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun providesContentResolver(application: Application) = application.applicationContext.contentResolver

    @Provides
    fun providesRealMediaSessionConnection(application: Application) =
        RealMediaSessionConnection(application, ComponentName(application, TimberMusicService::class.java))
}

package com.kafka.player

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import com.kafka.player.playback.player.Player
import com.kafka.player.playback.player.RealPlayer
import com.kafka.player.timber.permissions.PermissionsManager
import com.kafka.player.timber.permissions.RealPermissionsManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
abstract class MediaModuleBinds {

    @Binds
    abstract fun bindPlayer(realPlayer: RealPlayer): Player

    @Binds
    abstract fun bindPermissionsManager(realPermissionsManager: RealPermissionsManager): PermissionsManager
}

@Module
@InstallIn(ApplicationComponent::class)
class MediaModule {
    @Provides
    fun providesNotificationService(application: Application): NotificationManager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    fun providesContentResolver(application: Application) = application.applicationContext.contentResolver
}

package com.kafka.player.injection

import android.content.Context
import com.kafka.data.injection.ApplicationContext
import com.kafka.player.playback.MusicCacheManager
import com.kafka.player.playback.Playback
import com.kafka.player.playback.exo.ExoPlayback
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

const val DOWNLOAD_DIR = "download_dir"

@InstallIn(ApplicationComponent::class)
@Module(includes = [PlayerModuleBinds::class])
class PlayerModule {
    @Provides
    fun provideDownloadDir(@ApplicationContext context: Context) = MusicCacheManager(context, DOWNLOAD_DIR)
}

@InstallIn(ApplicationComponent::class)
@Module
internal abstract class PlayerModuleBinds {
    @Binds
    abstract fun bindPlayback(exoPlayback: ExoPlayback): Playback
}

package com.kafka.player.injection

import android.content.Context
import com.kafka.data.injection.ApplicationContext
import com.kafka.player.playback.MusicCacheManager
import com.kafka.player.playback.Playback
import com.kafka.player.playback.exo.ExoPlayback
import dagger.Binds
import dagger.Module
import dagger.Provides

const val DOWNLOAD_DIR = "download_dir"

@Module(includes = [PlayerModuleBinds::class])
class PlayerModule {
    @Provides
    fun provideDownloadDir(@ApplicationContext context: Context) = MusicCacheManager(context, DOWNLOAD_DIR)
}

@Module
internal abstract class PlayerModuleBinds {
    @Binds
    abstract fun bindPlayback(exoPlayback: ExoPlayback): Playback
}

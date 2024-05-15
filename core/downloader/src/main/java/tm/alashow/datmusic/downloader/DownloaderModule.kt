/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import android.content.Context
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.downloaderType
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.FetchNotificationManager
import com.tonyodev.fetch2okhttp.OkHttpDownloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton
import com.tonyodev.fetch2core.Downloader as FetchDownloader

@InstallIn(SingletonComponent::class)
@Module
class DownloaderModule {

    @Provides
    internal fun provideDownloader(downloader: DownloaderImpl): Downloader = downloader

    @Provides
    @Singleton
    fun fetchNotificationManager(
        @ApplicationContext context: Context,
    ): FetchNotificationManager = DownloaderNotificationManager(context)

    @Provides
    @Singleton
    fun provideFetch(
        @ApplicationContext context: Context,
        @Named("downloader") okHttpClient: OkHttpClient,
        notificationManager: FetchNotificationManager,
        remoteConfig: RemoteConfig,
    ): Fetch {
        val downloaderType = if (remoteConfig.downloaderType() == "parallel") {
            FetchDownloader.FileDownloaderType.PARALLEL
        } else {
            FetchDownloader.FileDownloaderType.SEQUENTIAL
        }

        val fetcherConfig = FetchConfiguration.Builder(context)
            .setNamespace("downloads")
            .setAutoRetryMaxAttempts(4)
            .enableRetryOnNetworkGain(true)
            .enableAutoStart(true)
            .setNotificationManager(notificationManager)
            .setHttpDownloader(OkHttpDownloader(okHttpClient, downloaderType))
            .build()
        Fetch.Impl.setDefaultInstanceConfiguration(fetcherConfig)
        return Fetch.Impl.getInstance(fetcherConfig)
    }
}

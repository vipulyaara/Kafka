/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import android.app.Application
import com.kafka.remote.config.RemoteConfig
import com.kafka.remote.config.downloaderType
import com.tonyodev.fetch2.Fetch
import com.tonyodev.fetch2.FetchConfiguration
import com.tonyodev.fetch2.FetchNotificationManager
import com.tonyodev.fetch2okhttp.OkHttpDownloader
import me.tatarka.inject.annotations.Component
import me.tatarka.inject.annotations.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.kafka.base.ApplicationScope
import org.kafka.base.Named
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.minutes
import com.tonyodev.fetch2core.Downloader as FetchDownloader

@Component
@ApplicationScope
interface DownloaderModule {

    @Provides
    fun provideDownloader(downloader: DownloaderImpl): Downloader = downloader

    @Provides
    @ApplicationScope
    fun fetchNotificationManager(
        context: Application,
    ): FetchNotificationManager = DownloaderNotificationManager(context)

    @Provides
    @Named("downloader")
    fun downloaderOkHttp(
        cache: Cache,
    ) = OkHttpClient.Builder()
        .cache(cache)
        .retryOnConnectionFailure(true)
        .readTimeout(Config.DOWNLOADER_TIMEOUT, TimeUnit.MILLISECONDS)
        .writeTimeout(Config.DOWNLOADER_TIMEOUT, TimeUnit.MILLISECONDS)
        .build()

    @Provides
    @ApplicationScope
    fun provideFetch(
        context: Application,
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
            .setDownloadConcurrentLimit(1)
            .setAutoRetryMaxAttempts(4)
            .enableRetryOnNetworkGain(true)
            .enableAutoStart(true)
            .setNotificationManager(notificationManager)
            .setHttpDownloader(OkHttpDownloader(okHttpClient, downloaderType))
            .build()
        Fetch.Impl.setDefaultInstanceConfiguration(fetcherConfig)
        return Fetch.Impl.getInstance(fetcherConfig)
    }

    object Config {
        val DOWNLOADER_TIMEOUT = 7.minutes.inWholeMilliseconds
    }
}

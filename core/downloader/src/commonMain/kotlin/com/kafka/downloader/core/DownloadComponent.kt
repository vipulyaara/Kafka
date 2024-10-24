package com.kafka.downloader.core

import com.kafka.base.ApplicationScope
import me.tatarka.inject.annotations.Provides

interface DownloadComponent {
    @ApplicationScope
    @Provides
    fun provideAppReviewManager(impl: KtorDownloader): Downloader = impl
}

package com.kafka.downloader.core

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DownloadItem @Inject constructor(
    private val downloader: KtorDownloader,
    private val dispatchers: CoroutineDispatchers
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) = withContext(dispatchers.io) {
        downloader.download(params)
    }
}

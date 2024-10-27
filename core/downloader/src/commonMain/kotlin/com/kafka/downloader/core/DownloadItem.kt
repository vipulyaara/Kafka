package com.kafka.downloader.core

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class DownloadItem(
    private val downloader: Downloader,
    private val dispatchers: CoroutineDispatchers
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) = withContext(dispatchers.io) {
        downloader.download(params)
    }
}

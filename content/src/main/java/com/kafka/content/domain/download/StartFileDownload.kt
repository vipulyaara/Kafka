package com.kafka.content.domain.download

import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.kafka.content.work.DownloadFileWorker
import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class StartFileDownload @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope
) : Interactor<String>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: String) {
        val request = OneTimeWorkRequest.Builder(DownloadFileWorker::class.java)
            .addTag(DownloadFileWorker.TAG)
            .setInputData(DownloadFileWorker.buildData(params))
            .build()
        WorkManager.getInstance().enqueue(request)
    }
}

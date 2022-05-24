package org.kafka.domain.interactors

import org.kafka.base.AppCoroutineDispatchers
import org.kafka.base.domain.Interactor
import javax.inject.Inject

class StartFileDownload @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<String>() {

    override suspend fun doWork(params: String) {
//        val request = OneTimeWorkRequest.Builder(DownloadFileWorker::class.java)
//            .addTag(DownloadFileWorker.TAG)
//            .setInputData(DownloadFileWorker.buildData(params))
//            .build()
//        WorkManager.getInstance().enqueue(request)
    }
}

package com.kafka.content.work

import android.content.Context
import androidx.hilt.Assisted
import androidx.hilt.work.WorkerInject
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import com.data.base.model.getOrThrow
import com.kafka.content.data.download.DownloadRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class DownloadFileWorker @WorkerInject constructor(
    @Assisted params: WorkerParameters,
    @Assisted @ApplicationContext context: Context,
    private val downloadRepository: DownloadRepository
) : CoroutineWorker(context, params) {
    companion object {
        const val TAG = "download-file"
        private const val PARAM_URL = "url"

        fun buildData(url: String) = Data.Builder()
            .putString(PARAM_URL, url)
            .build()
    }

    override suspend fun doWork(): Result {
        val url = inputData.getString(PARAM_URL)
        downloadRepository.downloadFile(url!!)
            .getOrThrow()
            .let { response ->
                val bytes = response.bytes()
            }
        return Result.success()
    }

}

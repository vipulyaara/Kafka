package tm.alashow.datmusic.downloader

import com.tonyodev.fetch2.Download
import com.tonyodev.fetch2.Fetch
import com.kafka.base.debug
import com.kafka.base.errorLog
import javax.inject.Inject

class DownloadRetryManager @Inject constructor(private val fetch: Fetch) {

    fun retryFailedDownload(download: Download) {
        val retryCount = download.extras.getInt(RETRY_COUNT_KEY, 0)

        if (retryCount < MAX_RETRY_COUNT) {
            val newRetryCount = retryCount + 1
            fetch.getDownload(download.id) { updatedDownload ->
                if (updatedDownload != null) {
                    val updatedExtras = updatedDownload.extras.toMutableExtras().apply {
                        putInt(RETRY_COUNT_KEY, newRetryCount)
                    }
                    fetch.updateRequest(
                        updatedDownload.request.id,
                        updatedDownload.request.apply { extras = updatedExtras })
                    fetch.retry(download.id)
                    debug { "Retrying download ${download.id}, attempt $newRetryCount" }
                }
            }
        } else {
            errorLog { "Download ${download.id} failed after $MAX_RETRY_COUNT attempts" }
        }
    }

    private companion object {
        const val RETRY_COUNT_KEY = "retry_count"
        const val MAX_RETRY_COUNT = 3
    }
}

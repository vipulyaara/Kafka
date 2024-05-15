//package tm.alashow.datmusic.downloader
//
//import android.app.Application
//import android.app.DownloadManager
//import android.content.Context
//import android.net.Uri
//import android.os.Environment
//import com.kafka.data.dao.DownloadRequestsDao
//import com.tonyodev.fetch2.Download
//import com.tonyodev.fetch2.Request
//import com.tonyodev.fetch2.Status
//import org.kafka.base.debug
//import tm.alashow.datmusic.downloader.manager.DownloadEnqueueResult
//import tm.alashow.datmusic.downloader.manager.DownloadEnqueueSuccessful
//import javax.inject.Inject
//import kotlin.coroutines.suspendCoroutine
//
//class RawDownloadManager @Inject constructor(
//    application: Application,
//    private val downloadRequestsDao: DownloadRequestsDao
//) : tm.alashow.datmusic.downloader.manager.DownloadManager<Int, Request, Status, Download> {
//    private val downloadManager =
//        application.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//
//    override suspend fun enqueue(request: Request): DownloadEnqueueResult<Request> {
//        downloadFile(request.file, "downloading..", request.url)
//        return DownloadEnqueueSuccessful(request)
//    }
//
//    private suspend fun downloadFile(fileName: String, desc: String, url: String) {
//        // fileName -> fileName with extension
////        val newurl = "https://ia802802.us.archive.org/18/items/dli.sangeet.thumri.7/03%20Kahee%20Sataay%20Mohe%20Saan%20varia.mp3"
//        val request = DownloadManager.Request(Uri.parse(url.replace(" ", "%20")))
//            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
//            .setTitle(fileName)
//            .setDescription(desc)
//            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
//            .setAllowedOverMetered(true)
//            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "fileName.mp3")
//            .setMimeType("*/*")
//        downloadManager.enqueue(request)
//    }
//
//    override suspend fun getDownload(id: Int): Download? = suspendCoroutine { continuation ->
////        fetch.getDownload(id) { continuation.resume(it) }
//    }
//
//    override suspend fun getDownloadsWithIdsAndStatuses(ids: Set<Int>): List<Download> {
////        val requests = downloadRequestsDao.getByRequestIds(ids.toList())
//        return emptyList()
//    }
//
//    override suspend fun resume(id: Int) {
////        fetch.resume(id)
//    }
//
//    override suspend fun pause(ids: List<Int>) {
////        fetch.pause(ids)
//    }
//
//    override suspend fun resume(ids: List<Int>) {
////        fetch.resume(ids)
//    }
//
//    override suspend fun cancel(ids: List<Int>) {
//        ids.forEach { id ->
//            downloadManager.remove(id.toLong())
//        }
//    }
//
//    override suspend fun remove(ids: List<Int>) {
//        ids.forEach { id ->
//            downloadManager.remove(id.toLong())
//        }
//    }
//
//    override suspend fun retry(ids: List<Int>) {
//        ids.forEach { id ->
//            downloadManager.remove(id.toLong())
//        }
//    }
//
//    override suspend fun delete(ids: List<Int>) {
//        ids.forEach { id ->
//            downloadManager.remove(id.toLong())
//        }
//    }
//
//    override suspend fun delete(id: Int) {
//        downloadManager.remove(id.toLong())
//    }
//
//    private fun checkStatus(downloadReference: Long): Int {
//        val myDownloadQuery: DownloadManager.Query = DownloadManager.Query()
//        //set the query filter to our previously Enqueued download
//        myDownloadQuery.setFilterById(downloadReference)
//        //Query the download manager about downloads that have been requested.
//        val cursor = downloadManager.query(myDownloadQuery)
//        if (cursor.moveToFirst()) {
//            //checkStatus(cursor);
//
//            //column for status
//            val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
//            val status = cursor.getInt(columnIndex)
//            //column for reason code if the download failed or paused
//            val columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON)
//            val reason = cursor.getInt(columnReason)
//            //get the download filename
//            val filenameIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME)
//            val filename = cursor.getString(filenameIndex)
//            var statusText = ""
//            var reasonText = ""
//            when (status) {
//                DownloadManager.STATUS_FAILED -> {
//                    statusText = "STATUS_FAILED"
//                    when (reason) {
//                        DownloadManager.ERROR_CANNOT_RESUME -> reasonText = "ERROR_CANNOT_RESUME"
//                        DownloadManager.ERROR_DEVICE_NOT_FOUND -> reasonText =
//                            "ERROR_DEVICE_NOT_FOUND"
//
//                        DownloadManager.ERROR_FILE_ALREADY_EXISTS -> reasonText =
//                            "ERROR_FILE_ALREADY_EXISTS"
//
//                        DownloadManager.ERROR_FILE_ERROR -> reasonText = "ERROR_FILE_ERROR"
//                        DownloadManager.ERROR_HTTP_DATA_ERROR -> reasonText =
//                            "ERROR_HTTP_DATA_ERROR"
//
//                        DownloadManager.ERROR_INSUFFICIENT_SPACE -> reasonText =
//                            "ERROR_INSUFFICIENT_SPACE"
//
//                        DownloadManager.ERROR_TOO_MANY_REDIRECTS -> reasonText =
//                            "ERROR_TOO_MANY_REDIRECTS"
//
//                        DownloadManager.ERROR_UNHANDLED_HTTP_CODE -> reasonText =
//                            "ERROR_UNHANDLED_HTTP_CODE"
//
//                        DownloadManager.ERROR_UNKNOWN -> reasonText = "ERROR_UNKNOWN"
//                    }
//                }
//
//                DownloadManager.STATUS_PAUSED -> {
//                    statusText = "STATUS_PAUSED"
//                    when (reason) {
//                        DownloadManager.PAUSED_QUEUED_FOR_WIFI -> reasonText =
//                            "PAUSED_QUEUED_FOR_WIFI"
//
//                        DownloadManager.PAUSED_UNKNOWN -> reasonText = "PAUSED_UNKNOWN"
//                        DownloadManager.PAUSED_WAITING_FOR_NETWORK -> reasonText =
//                            "PAUSED_WAITING_FOR_NETWORK"
//
//                        DownloadManager.PAUSED_WAITING_TO_RETRY -> reasonText =
//                            "PAUSED_WAITING_TO_RETRY"
//                    }
//                }
//
//                DownloadManager.STATUS_PENDING -> statusText = "STATUS_PENDING"
//                DownloadManager.STATUS_RUNNING -> statusText = "STATUS_RUNNING"
//                DownloadManager.STATUS_SUCCESSFUL -> {
//                    statusText = "STATUS_SUCCESSFUL"
//                    reasonText = "Filename:\n$filename"
//                }
//            }
//
//            debug { "Download Status: $statusText\nReason: $reasonText" }
//
//            return status
//        }
//        return 0
//    }
//
//    override suspend fun getDownloads(): List<Download> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun remove(id: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun retry(id: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun cancel(id: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun pause(id: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getDownloadsWithStatuses(statuses: List<Status>): List<Download> {
//        TODO("Not yet implemented")
//    }
//}
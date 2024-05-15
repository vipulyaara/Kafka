//package tm.alashow.datmusic.downloader.manager
//
//import com.kafka.data.feature.DownloadsRepository
//import javax.inject.Inject
//
//class HttpDownloadManager @Inject constructor(
//    private val downloadsRepository: DownloadsRepository
//) : DownloadManager<Int, Request, Status, Download> {
//
//    override suspend fun enqueue(request: Request) {
//        downloadsRepository.downloadFile(request.url)
//    }
//
//    override suspend fun getDownloads(): List<Download> {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getDownload(id: Int): Download? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun delete(ids: List<Int>) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun delete(id: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun remove(ids: List<Int>) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun retry(ids: List<Int>) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun cancel(ids: List<Int>) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun resume(ids: List<Int>) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun pause(ids: List<Int>) {
//        TODO("Not yet implemented")
//    }
//}
//
//data class Request(val url: String, val fileName: String)
//
//data class Download(
//    val id: Int,
//    val request: Request,
//    val status: Status,
//    val progress: Int,
//    val total: Long,
//    val downloaded: Long,
//)
//
//enum class Status {
//    PENDING,
//    DOWNLOADING,
//    PAUSED,
//    COMPLETED,
//    FAILED,
//    CANCELLED,
//}

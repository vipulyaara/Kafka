package com.kafka.content.data.download

import android.os.Environment
import com.data.base.extensions.debug
import com.kafka.content.data.item.ItemRemoteDataSource
import com.kafka.data.dao.ItemLocalDataSource
import okhttp3.ResponseBody
import java.io.*
import javax.inject.Inject

class DownloadRepository @Inject constructor(
    private val itemLocalDataSource: ItemLocalDataSource,
    private val itemRemoteDataSource: ItemRemoteDataSource
) {

    suspend fun downloadFile(url: String) = itemRemoteDataSource.downloadFile(url)

    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            val futureStudioIconFile =
                File(Environment.getExternalStorageDirectory().toString() + File.separator.toString() + "download")
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(futureStudioIconFile)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    debug { "file download: $fileSizeDownloaded of $fileSize" }
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            false
        }
    }

}

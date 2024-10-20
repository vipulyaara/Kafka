package tm.alashow.datmusic.downloader

import com.kafka.data.entities.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import me.tatarka.inject.annotations.Provides
import java.io.FileInputStream
import java.io.InputStream

actual interface DownloaderModule {

    // todo: kmp implement
    @Provides
    fun provideDownloader(): Downloader = object : Downloader {
        override val newDownloadId: Flow<String>
            get() = TODO("Not yet implemented")
        override val downloadLocation: Flow<String?>
            get() = flowOf("")
        override val downloaderEvents: Flow<DownloaderEvent>
            get() = TODO("Not yet implemented")

        override fun clearDownloaderEvents() {
            TODO("Not yet implemented")
        }

        override fun getDownloaderEvents(): List<DownloaderEvent> {
            TODO("Not yet implemented")
        }

        override suspend fun enqueueFile(fileId: String): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun enqueueFile(file: File): Boolean {
            TODO("Not yet implemented")
        }

        override suspend fun pause(vararg downloadInfoIds: Int) {
            TODO("Not yet implemented")
        }

        override suspend fun resume(vararg downloadInfoIds: Int) {
            TODO("Not yet implemented")
        }

        override suspend fun cancel(vararg downloadInfoIds: Int) {
            TODO("Not yet implemented")
        }

        override suspend fun retry(vararg downloadInfoIds: Int) {
            TODO("Not yet implemented")
        }

        override suspend fun delete(vararg downloadInfoIds: Int) {
            TODO("Not yet implemented")
        }

        override suspend fun findAudioDownload(fileId: String): File? {
            TODO("Not yet implemented")
        }

        override val hasDownloadsLocation: Flow<Boolean>
            get() = TODO("Not yet implemented")

        override fun requestNewDownloadsLocation() {
            TODO("Not yet implemented")
        }

        override suspend fun setDownloadsLocation(folder: java.io.File) {
            TODO("Not yet implemented")
        }

        override suspend fun setDownloadsLocation(uri: String?) {
            TODO("Not yet implemented")
        }

        override suspend fun resetDownloadsLocation() {
            TODO("Not yet implemented")
        }

        override suspend fun getInputStreamFromUri(uri: String): InputStream? {
            TODO("Not yet implemented")
        }
    }
}

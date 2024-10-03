package tm.alashow.datmusic.downloader.interactors

import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveDownloadByFileId @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeDownloadedFiles: ObserveDownloadedFiles,
) : SubjectInteractor<String, ItemWithDownload?>() {

    @Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
    override fun createObservable(fileId: String): Flow<ItemWithDownload?> {
        return observeDownloadedFiles.createObservable(Unit).map { downloadItems ->
            downloadItems.firstOrNull { it.file.fileId == fileId }
        }.flowOn(dispatchers.io)
    }
}

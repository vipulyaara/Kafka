package tm.alashow.datmusic.downloader.interactors

import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.kafka.base.CoroutineDispatchers
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class ObserveDownloadedItems @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val observeDownloadedFiles: ObserveDownloadedFiles,
) : SubjectInteractor<Unit, List<ItemWithDownload>>() {

    override fun createObservable(params: Unit): Flow<List<ItemWithDownload>> {
        return observeDownloadedFiles.createObservable(Unit)
            .map { it.distinctBy { it.item.itemId } }
            .flowOn(dispatchers.io)
    }
}

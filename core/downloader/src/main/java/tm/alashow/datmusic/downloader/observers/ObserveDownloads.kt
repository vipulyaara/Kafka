/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader.observers

import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.entities.DownloadRequest
import com.tonyodev.fetch2.Download
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.kafka.base.domain.SubjectInteractor
import tm.alashow.datmusic.downloader.DownloadItems
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.downloader.FileDownloadItem
import tm.alashow.datmusic.downloader.interactors.SearchDownloads
import tm.alashow.datmusic.downloader.manager.FetchDownloadManager
import javax.inject.Inject

class ObserveDownloads @Inject constructor(
    private val fetcher: FetchDownloadManager,
    private val dao: DownloadRequestsDao,
    private val searchDownloads: SearchDownloads,
) : SubjectInteractor<ObserveDownloads.Params, DownloadItems>() {

    data class Params(val query: String = "") {
        val hasQuery get() = query.isNotBlank()
    }

    private fun fetcherDownloads(
        downloadRequests: List<DownloadRequest> = emptyList()
    ): Flow<List<Pair<DownloadRequest, Download>>> = flow {
        val requestsById = downloadRequests.associateBy { it.requestId }
        while (true) {
            fetcher.getDownloads()
                .map { requestsById.getValue(it.id) to it }
                .also { emit(it) }
            delay(Downloader.DOWNLOADS_STATUS_REFRESH_INTERVAL)
        }
    }.distinctUntilChanged()

    override fun createObservable(params: Params): Flow<DownloadItems> {
        return flow {
            val items = fetcher.getDownloads().map { FileDownloadItem.from(it) }
            emit(DownloadItems(items))
        }
    }
}

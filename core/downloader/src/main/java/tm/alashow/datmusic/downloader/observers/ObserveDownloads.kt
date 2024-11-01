/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader.observers

import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.entities.DownloadRequest
import com.kafka.data.feature.item.DownloadInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import tm.alashow.datmusic.downloader.Downloader
import tm.alashow.datmusic.downloader.FileDownloadItem
import tm.alashow.datmusic.downloader.manager.FetchDownloadManager
import tm.alashow.datmusic.downloader.mapper.DownloadInfoMapper
import javax.inject.Inject

actual class ObserveDownloads @Inject constructor(
    private val fetcher: FetchDownloadManager,
    private val dao: DownloadRequestsDao,
    private val downloadInfoMapper: DownloadInfoMapper
) {
    actual fun execute(): Flow<DownloadItems> {
        val downloadsRequestsFlow: Flow<List<DownloadRequest>> = dao.entries()
        return downloadsRequestsFlow.flatMapLatest { fetcherDownloads(it) }
            .map {
                val audioDownloads = it
                    .filter { pair -> pair.first.entityType == DownloadRequest.Type.Audio }
                    .map { (request, info) -> FileDownloadItem.from(request, info) }
                DownloadItems(audioDownloads)
            }
    }

    private fun fetcherDownloads(
        downloadRequests: List<DownloadRequest> = emptyList(),
    ): Flow<List<Pair<DownloadRequest, DownloadInfo>>> = flow {
        val requestsById = downloadRequests.associateBy { it.requestId }
        while (true) {
            fetcher.getDownloadsWithIdsAndStatuses(ids = requestsById.keys)
                .map { requestsById.getValue(it.id) to downloadInfoMapper.map(it) }
                .also { emit(it) }
            delay(Downloader.DOWNLOADS_STATUS_REFRESH_INTERVAL)
        }
    }.distinctUntilChanged()
}

/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader.interactors

import com.kafka.data.dao.DownloadRequestsDao
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.DownloadRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.kafka.base.domain.SubjectInteractor
import javax.inject.Inject

class SearchDownloads @Inject constructor(
    private val downloadRequestsDao: DownloadRequestsDao,
    private val fileDao: FileDao,
) : SubjectInteractor<String, List<DownloadRequest>>() {
    override fun createObservable(params: String): Flow<List<DownloadRequest>> {
        return fileDao.entriesByTitle(params).map { files ->
            downloadRequestsDao.getByIds(files.map { it.fileId })
        }
    }
}

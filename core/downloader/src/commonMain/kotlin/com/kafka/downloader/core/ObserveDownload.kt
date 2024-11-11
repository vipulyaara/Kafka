package com.kafka.downloader.core

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.SubjectInteractor
import com.kafka.data.dao.DownloadDao
import com.kafka.data.entities.Download
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveDownload(
    private val downloadDao: DownloadDao,
    private val dispatchers: CoroutineDispatchers
) : SubjectInteractor<String, Download?>() {

    override fun createObservable(params: String): Flow<Download?> {
        return downloadDao.observe(params)
            .flowOn(dispatchers.io)
    }
}

package com.kafka.reader.data

import com.data.base.api.ArchiveService
import com.data.base.extensions.executeWithRetry
import com.data.base.extensions.toResult
import javax.inject.Inject

class ReaderDataSource @Inject constructor(private val archiveService: ArchiveService) {
    suspend fun downloadFile(url: String) = archiveService.downloadFileWithDynamicUrlSync(url)
        .executeWithRetry()
        .toResult { it.byteStream() }
}

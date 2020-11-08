package com.kafka.reader.data

import com.kafka.data.api.ArchiveService
import com.kafka.data.extensions.executeWithRetry
import com.kafka.data.extensions.toResult
import javax.inject.Inject

class ReaderDataSource @Inject constructor(private val archiveService: ArchiveService) {
    suspend fun downloadFile(url: String) = archiveService.downloadFileWithDynamicUrlSync(url)
        .executeWithRetry()
        .toResult { it.byteStream() }
}

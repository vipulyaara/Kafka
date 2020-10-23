package com.kafka.reader.data

import com.data.base.model.getOrThrow
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReaderRepository @Inject constructor(private val readerDataSource: ReaderDataSource) {
    suspend fun downloadFile(url: String): InputStream {
        return readerDataSource.downloadFile(url).getOrThrow()
    }
}

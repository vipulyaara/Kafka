package com.kafka.reader.domain

import com.data.base.ResultInteractor
import com.kafka.reader.data.ReaderRepository
import java.io.InputStream
import javax.inject.Inject

class ReadFromUrl @Inject constructor(
    private val readerRepository: ReaderRepository
) : ResultInteractor<String, InputStream>() {
    override suspend fun doWork(params: String): InputStream = readerRepository.downloadFile(params)
}

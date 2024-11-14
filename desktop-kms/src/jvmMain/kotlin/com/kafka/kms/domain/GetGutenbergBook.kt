package com.kafka.kms.domain

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.kms.data.models.GutenbergBook
import com.kafka.kms.data.remote.GutendexService
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class GetGutenbergBook(
    private val gutendexService: GutendexService,
    private val dispatchers: CoroutineDispatchers
) : Interactor<String, GutenbergBook>() {
    override suspend fun doWork(params: String): GutenbergBook {
        return withContext(dispatchers.io) {
            gutendexService.getBookById(params)
        }
    }
}

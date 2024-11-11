package com.kafka.domain.interactors.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class AddToBookshelf(
    private val dispatchers: CoroutineDispatchers
): Interactor<AddToBookshelf.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {

        }
    }

    data class Params(val itemId: String, val bookshelfId: String)
}

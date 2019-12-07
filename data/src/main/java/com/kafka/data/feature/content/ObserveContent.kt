package com.kafka.data.feature.content

import com.kafka.data.data.interactor.SubjectInteractor
import com.kafka.data.entities.Content
import com.kafka.data.util.AppCoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Interactor for updating the homepage.
 * */
class ObserveContent @Inject constructor(
    private val dispatchers: AppCoroutineDispatchers,
    private val repository: ContentRepository
) : SubjectInteractor<ObserveContent.Params, List<Content>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Params): Flow<List<Content>> {
        return when (params) {
            is Params.ByCreator -> repository.observeQueryByCreator(params.creator)
            is Params.ByCollection -> repository.observeQueryByCollection(params.collection)
            is Params.ByGenre -> repository.observeQueryByGenre(params.genre)
        }
    }

    sealed class Params {
        class ByCreator(val creator: String) : Params()
        class ByCollection(val collection: String) : Params()
        class ByGenre(val genre: String) : Params()
    }

    data class ExecuteParams(val id: Long = 0)
}

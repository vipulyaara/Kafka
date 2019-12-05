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
) : SubjectInteractor<UpdateContent.Params, List<Content>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: UpdateContent.Params): Flow<List<Content>> {
        return when (params) {
            is UpdateContent.Params.ByCreator -> repository.observeQueryByCreator(params.creator)
            is UpdateContent.Params.ByCollection -> repository.observeQueryByCollection(params.collection)
            is UpdateContent.Params.ByGenre -> repository.observeQueryByGenre(params.genre)
        }
    }
}

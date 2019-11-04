package com.kafka.data.feature.config

import com.kafka.data.model.LanguageModel
import com.kafka.data.util.AppCoroutineDispatchers
import io.reactivex.Flowable
import kotlinx.coroutines.CoroutineDispatcher

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class UpdateContentLanguage constructor(
    dispatchers: AppCoroutineDispatchers,
    private val repository: ContentLanguageRepository
) : SubjectInteractor<UpdateContentLanguage.Params, UpdateContentLanguage.ExecuteParams, List<LanguageModel>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override suspend fun execute(
        params: Params,
        executeParams: ExecuteParams
    ) {
        repository.updateLanguages(params.languageIds)
    }

    override fun createObservable(params: Params): Flowable<List<LanguageModel>> {
        return repository.observeForFlowable()
    }

    data class Params(val languageIds: String)

    data class ExecuteParams(val id: Long = 0)
}

package com.kafka.data.feature.config

import com.kafka.data.feature.Repository
import com.kafka.data.model.LanguageModel
import com.kafka.data.model.data.Result
import com.kafka.data.model.data.Success

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class ContentLanguageRepository constructor(
    private val localStore: LocalContentLanguageStore
) : Repository {

    fun observeForFlowable() = localStore.languageFlowable()

    suspend fun updateLanguages(languageIds: String): Result<List<LanguageModel>> {
        //TODO: filter
        val languages = localStore.languages()
            .filter { languageIds.contains(it.languageId) }
        localStore.insertLanguages(languages)
        return Success(languages)
    }
}

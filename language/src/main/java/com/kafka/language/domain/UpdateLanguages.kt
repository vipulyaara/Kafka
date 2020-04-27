package com.kafka.language.domain

import com.data.base.Interactor
import com.kafka.data.entities.Language
import com.kafka.data.injection.ProcessLifetime
import com.kafka.language.data.LanguageRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateLanguages @Inject constructor(
    dispatchers: com.data.base.AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val languageRepository: LanguageRepository
) : Interactor<UpdateLanguages.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        languageRepository.saveLanguages(params.languages)
    }

    data class Params(val languages: List<Language>)
}

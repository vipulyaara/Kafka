package com.kafka.content.domain.language

import com.data.base.AppCoroutineDispatchers
import com.data.base.Interactor
import com.kafka.content.data.language.LanguageRepository
import com.kafka.data.entities.Language
import com.kafka.data.injection.ProcessLifetime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.plus
import javax.inject.Inject

class UpdateLanguages @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    @ProcessLifetime processScope: CoroutineScope,
    private val languageRepository: LanguageRepository
) : Interactor<UpdateLanguages.Params>() {
    override val scope: CoroutineScope = processScope + dispatchers.io

    override suspend fun doWork(params: Params) {
        languageRepository.saveLanguages(params.languages)
    }

    data class Params(val languages: List<Language>)
}

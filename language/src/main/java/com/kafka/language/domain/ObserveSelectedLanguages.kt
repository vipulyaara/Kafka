package com.kafka.language.domain

import com.data.base.AppCoroutineDispatchers
import com.data.base.SubjectInteractor
import com.kafka.data.entities.Language
import com.kafka.language.data.LanguageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveSelectedLanguages @Inject constructor(
    dispatchers: AppCoroutineDispatchers,
    private val languageRepository: LanguageRepository
) : SubjectInteractor<Unit, List<Language>>() {
    override val dispatcher: CoroutineDispatcher = dispatchers.io

    override fun createObservable(params: Unit): Flow<List<Language>> {
        return languageRepository.selectedLanguagesObservable()
    }
}

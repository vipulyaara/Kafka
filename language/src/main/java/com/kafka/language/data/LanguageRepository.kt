package com.kafka.language.data

import com.kafka.data.data.db.dao.LanguageDao
import com.kafka.data.entities.Language
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LanguageRepository @Inject constructor(private val languageDao: LanguageDao) {
    suspend fun saveLanguages(languages: List<Language>) {
        languageDao.insertAll(languages)
    }

    fun selectedLanguagesObservable() = languageDao.languageFlow()
}

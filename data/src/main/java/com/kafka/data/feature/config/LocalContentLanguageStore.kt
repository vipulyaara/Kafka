package com.kafka.data.feature.config

import com.kafka.data.data.db.DatabaseTransactionRunner
import com.kafka.data.data.db.dao.LanguageDao
import com.kafka.data.model.LanguageModel
import io.reactivex.Flowable

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 */
class LocalContentLanguageStore constructor(
    private val transactionRunner: DatabaseTransactionRunner,
    private val languageDao: LanguageDao
) {
    fun languageFlowable(): Flowable<List<LanguageModel>> {
        return languageDao.languageFlowable()
    }

    fun languages(): List<LanguageModel> {
        return languageDao.languages()
    }

    fun insertLanguages(languages: List<LanguageModel>) = transactionRunner {
        languageDao.insertLanguages(languages)
    }
}

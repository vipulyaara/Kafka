package com.kafka.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.kafka.data.entities.Language
import kotlinx.coroutines.flow.Flow

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class LanguageDao : EntityDao<Language> {
    @Query("select * from Language")
    abstract fun languageFlow(): Flow<List<Language>>

    @Query("select * from Language where isSelected = 1")
    abstract fun selectLanguageFlow(): Flow<List<Language>>
}

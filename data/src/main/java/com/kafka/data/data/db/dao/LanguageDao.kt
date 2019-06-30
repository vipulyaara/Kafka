package com.kafka.data.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.LanguageModel
import io.reactivex.Flowable

/**
 * @author Vipul Kumar; dated 29/11/18.
 */
@Dao
abstract class LanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLanguages(items: List<LanguageModel>)

    @Query("select * from LanguageModel")
    abstract fun languageFlowable(): Flowable<List<LanguageModel>>

    @Query("select * from LanguageModel")
    abstract fun languages(): List<LanguageModel>
}

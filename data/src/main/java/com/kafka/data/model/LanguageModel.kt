package com.kafka.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Vipul Kumar; dated 2019-05-28.
 */
@Entity
data class LanguageModel(
    @PrimaryKey val languageId: String,
    val languageName: String,
    val isSelected: Boolean
) {

}

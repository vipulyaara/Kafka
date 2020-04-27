package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Vipul Kumar; dated 2019-05-28.
 */
@Entity
data class Language(
    @PrimaryKey val languageId: String,
    val languageName: String,
    val isSelected: Boolean = false
): BaseEntity

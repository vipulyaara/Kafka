package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author Vipul Kumar; dated 13/02/19.
 */
@Entity
data class SearchConfiguration constructor(
    @PrimaryKey val id: Long = 0,
    val recentSearches: List<String>? = null
) : BaseEntity

package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "followed_creator",
    indices = [
        Index(value = ["itemId"], unique = true),
    ],
    foreignKeys = [
        ForeignKey(
            entity = ItemDetail::class,
            parentColumns = arrayOf("itemId"),
            childColumns = arrayOf("itemId"),
        ),
    ],
)
data class FollowedCreator(
    @PrimaryKey val itemId: String = "",
) : BaseEntity

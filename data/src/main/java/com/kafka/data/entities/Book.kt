package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.model.book.BookResponse

/**
 * @author Vipul Kumar; dated 31/01/19.
 */

@Entity
data class Book(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val bookId: String?,
    val language: String? = null,
    val title: String? = null,
    val description: String? = null
) {
    fun generateStableId(): Long {
        return id
    }
}

fun BookResponse.toBook() = Book(
    bookId = this.id,
    language = this.language,
    title = this.title,
    description = this.description
)

package com.kafka.data.model.book

import com.squareup.moshi.Json

data class BookListResponse(
    @Json(name = "books")
    val books: List<BookResponse>? = null
)

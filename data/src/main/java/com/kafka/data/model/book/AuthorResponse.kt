package com.kafka.data.model.book

import com.squareup.moshi.Json

data class AuthorResponse(
    @Json(name = "dob")
    val dob: String,
    @Json(name = "dod")
    val dod: String,
    @Json(name = "first_name")
    val firstName: String,
    @Json(name = "id")
    val id: String,
    @Json(name = "last_name")
    val lastName: String
)

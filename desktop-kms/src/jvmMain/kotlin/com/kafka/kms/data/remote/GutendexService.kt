package com.kafka.kms.data.remote

import com.kafka.kms.data.models.GutenbergBook
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import me.tatarka.inject.annotations.Inject

class GutendexService @Inject constructor(
    private val client: HttpClient
) {
    companion object {
        private const val BASE_URL = "https://gutendex.com/books"
    }

    suspend fun getBookById(id: String) = client
        .get("$BASE_URL/$id")
        .body<GutenbergBook>()
} 
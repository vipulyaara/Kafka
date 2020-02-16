package com.kafka.data.data.api

import com.kafka.data.model.book.BookListResponse
import com.kafka.data.model.item.ItemDetailResponse
import com.kafka.data.model.item.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 * Configures the API module and provides services to interact with the APIs.
 */
interface ArchiveService {
    @GET("")
    fun getBooks(
        @Url url: String,
        @Query("format") format: String?
    ): Call<BookListResponse>

    @GET("advancedsearch.php")
    fun search(
        @Query("q", encoded = true) query: String?
    ): Call<SearchResponse>

    @GET("/metadata/{id}")
    fun getItemDetail(
        @Path("id") id: String?
    ): Call<ItemDetailResponse>
}

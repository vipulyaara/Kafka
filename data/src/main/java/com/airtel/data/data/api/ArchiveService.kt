package com.airtel.data.data.api

import com.airtel.data.model.book.BookListResponse
import com.airtel.data.model.book.BookResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 * configures the API module and provides services to interact with the APIs.
 */
interface ArchiveService {
    @GET("")
    fun getBooks(
        @Url url: String,
        @Query("format") format: String?
    ): Call<BookListResponse>

    @GET("")
    fun getBookDetail(
        @Url url: String,
        @Query("format") format: String?
    ): Call<BookListResponse>
}

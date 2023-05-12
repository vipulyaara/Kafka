package com.kafka.data.api

import com.kafka.data.model.item.ItemDetailResponse
import com.kafka.data.model.item.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 * Configures the API module and provides services to interact with the APIs.
 */
interface ArchiveService {
    @GET("advancedsearch.php")
    suspend fun search(
        @Query("q", encoded = true) query: String?,
        @Query("output") output: String = "json",
        @Query("rows") rows: String = "200",
        @Query("page") page: String = "1",
        @Query("sort") sort: String = "-downloads",
    ): SearchResponse

    @GET("/metadata/{id}")
    suspend fun getItemDetail(@Path("id") id: String?): ItemDetailResponse
}

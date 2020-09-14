package com.data.base.api

import com.data.base.model.item.ItemDetailResponse
import com.data.base.model.item.SearchResponse
import okhttp3.ResponseBody
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
    @GET("advancedsearch.php")
    fun search(@Query("q", encoded = true) query: String?): Call<SearchResponse>

    @GET("/metadata/{id}")
    fun getItemDetail(@Path("id") id: String?): Call<ItemDetailResponse>

    @GET
    fun downloadFileWithDynamicUrlSync(@Url fileUrl: String): Call<ResponseBody>
}

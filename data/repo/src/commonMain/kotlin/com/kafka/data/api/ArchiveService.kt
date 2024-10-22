package com.kafka.data.api

import com.kafka.base.ApplicationScope
import com.kafka.data.model.item.LibrivoxFileResponse
import com.kafka.data.model.item.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import me.tatarka.inject.annotations.Inject

/**
 * @author Vipul Kumar; dated 29/11/18.
 *
 * Configures the API module and provides services to interact with the APIs.
 */
@ApplicationScope
class ArchiveService @Inject constructor(private val httpClient: HttpClient) {
    suspend fun search(
        query: String?,
        output: String = "json",
        rows: String = "200",
        page: String = "1",
        sort: String = "-downloads",
    ): SearchResponse = httpClient.get {
        url("https://archive.org/advancedsearch.php")
        parameter("q", query)
        parameter("output", output)
        parameter("rows", rows)
        parameter("page", page)
        parameter("sort", sort)
    }.body()

    suspend fun getLibrivoxAudioTracks(id: String?): LibrivoxFileResponse = httpClient.get {
        url("https://librivox.org/api/feed/audiotracks?format=json&project_id=$id")
    }.body()
}

package com.kafka.data.api

import com.kafka.base.ApplicationScope
import com.kafka.data.model.item.LibrivoxFileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url
import me.tatarka.inject.annotations.Inject

@ApplicationScope
@Inject
class ArchiveService(private val httpClient: HttpClient) {
    suspend fun getLibrivoxAudioTracks(id: String?): LibrivoxFileResponse = httpClient.get {
        url(audioTracksUrl(id))
    }.body()

    private fun audioTracksUrl(id: String?) =
        "https://librivox.org/api/feed/audiotracks?format=json&project_id=$id"
}

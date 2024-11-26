package com.kafka.kms.data.remote

import com.kafka.kms.data.models.LibrivoxAudiobook
import com.kafka.kms.data.models.LibrivoxSection
import com.kafka.kms.domain.librivoxItemId
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.Serializable
import me.tatarka.inject.annotations.Inject

@Inject
class LibrivoxService(
    private val client: HttpClient
) {
    suspend fun getAudiobookById(id: String): LibrivoxAudiobook {
        val response =
            client.get("https://librivox.org/api/feed/audiobooks/?id=$id&format=json&extended=1")
        val result = response.body<LibrivoxApiResponse>()
        return result.books.first().toLibrivoxAudiobook()
    }
}

@Serializable
private data class LibrivoxApiResponse(
    val books: List<LibrivoxApiBook>
)

@Serializable
private data class LibrivoxApiBook(
    val id: String,
    val title: String,
    val description: String,
    val language: String,
    val authors: List<LibrivoxApiAuthor>,
    val sections: List<LibrivoxApiSection>,
    val url_zip_file: String,
    val url_project: String,
    val url_rss: String,
    val url_librivox: String,
    val totaltimesecs: Int,
    val totaltime: String,
    val genres: List<Genre> = listOf()
) {
    fun toLibrivoxAudiobook() = LibrivoxAudiobook(
        itemId = librivoxItemId(id),
        title = title,
        author = authors.firstOrNull()?.name ?: "Unknown Author",
        description = description,
        language = language,
        coverImage = null, // Librivox API doesn't provide cover images
        subjects = genres.map { it.name },
        sections = sections.map { it.toHighestBitrateSection() }
    )
}

@Serializable
data class Genre(val id: String, val name: String)

@Serializable
private data class LibrivoxApiAuthor(
    val id: String,
    val first_name: String,
    val last_name: String
) {
    val name: String get() = "$first_name $last_name".trim()
}

@Serializable
private data class LibrivoxApiSection(
    val section_number: Int,
    val title: String,
    val listen_url: String,
    val duration: String = "0",
    val formats: List<SectionFormat> = listOf()
) {
    fun toHighestBitrateSection(): LibrivoxSection {
        // Get the highest bitrate URL, fallback to listen_url if no formats available
        val highestBitrateUrl = formats
            .filter { it.url.isNotBlank() }
            .maxByOrNull { it.bitrate }
            ?.url ?: listen_url

        return LibrivoxSection(
            title = title,
            url = highestBitrateUrl,
            duration = duration,
            durationInSeconds = duration.split(":").let { parts ->
                when (parts.size) {
                    3 -> parts[0].toInt() * 3600 + parts[1].toInt() * 60 + parts[2].toInt()
                    2 -> parts[0].toInt() * 60 + parts[1].toInt()
                    else -> 0
                }
            }
        )
    }
}

@Serializable
private data class SectionFormat(
    val url: String = "",
    val codec: String = "",
    val bitrate: Int = 0
) 
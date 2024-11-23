package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kafka.data.model.MediaType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class File(
    @SerialName("file_id") @PrimaryKey val fileId: String,
    @SerialName("item_id") val itemId: String,
    @SerialName("item_title") val itemTitle: String?,
    @SerialName("size") val size: Long? = 0L,
    @SerialName("title") val title: String,
    @SerialName("media_type") val mediaType: MediaType,
    @SerialName("cover_image") val coverImage: String?,
    @SerialName("extension") val extension: String?,
    @SerialName("creators") val creators: List<String>,
    @SerialName("duration") val duration: Long? = null,
    @SerialName("format") val format: String,
    @SerialName("url") val url: String?,
    val position: Int = 0
) : BaseEntity {
    val name: String
        get() = title + "." + extension?.removePrefix(".")

    val creator: String
        get() = creators.take(5).firstOrNull().orEmpty()

    companion object {
        val audioExtensions = listOf("mp3", "wav", "m4a", "ogg", "aac", "flac")
        val textExtensions = listOf("pdf", "epub")
        val supportedExtensions = audioExtensions + textExtensions

        // extensions that show up in player, in order of preference
        val playableExtensions = listOf("mp3", "wav", "m4a", "ogg", "aac", "flac")
    }

    fun mapSize(): String {
        val step = 1000L
        val sizeKb = this.size?.run { this / step } ?: 0L
        val sizeMb = sizeKb / (step)
        val sizeGb = sizeMb / (step)
        val size = if (sizeGb > 1L) sizeGb else if (sizeMb > 1) sizeMb else sizeKb
        val label = if (sizeGb > 1L) "GB" else if (sizeMb > 1) "MB" else "KB"

        return if (size > 0) "$size $label" else ""
    }
}

fun String?.isTextExtension() = File.textExtensions.contains(this?.lowercase())
fun String?.isAudioExtension() = File.audioExtensions.contains(this?.lowercase())

fun File.isPlayable() = File.playableExtensions.contains(extension?.lowercase())
fun File.isAudio() = this.extension.isAudioExtension()
fun File.isText() = this.extension.isTextExtension()

fun File.nameWithoutExtension() = title.substringBeforeLast(".")

val fileFormatEpub = "readable/epub"
val fileFormatAudio = "readable/audio"
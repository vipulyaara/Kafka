package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val mediaTypeAudio = "audio"

@Entity
data class File(
    @PrimaryKey val fileId: String,
    val itemId: String,
    val itemTitle: String?,
    val size: Long?,
    val name: String,
    val extension: String?,
    val creator: String?,
    val time: String?,
    val format: String,
    val playbackUrl: String?,
    val readerUrl: String?,
    val downloadUrl: String?,
    val coverImage: String?,
    val localUri: String? = null
): BaseEntity {
    companion object {
        val supportedFiles = listOf("pdf", "mp3", "epub", "wav", "txt")
    }

    val title: String
        get() = name.removeSuffix(".$extension")

    val duration: Long
        get() = time?.let { mapDuration(it) } ?: 0L

    private fun mapDuration(duration: String): Long {
        var durationInSeconds = 0L
        duration.split(":")
            .takeIf { it.size > 1 }
            ?.reversed()
            ?.forEachIndexed { index, time ->
                durationInSeconds += time.toInt() * (index * 60).coerceAtLeast(1)
            }

        return durationInSeconds
    }

    fun mapSize(): String {
        val gap = 1000L
        val sizeKb = this.size?.run { this / gap } ?: 0L
        val sizeMb = sizeKb / (gap)
        val sizeGb = sizeMb / (gap)
        val size = if (sizeGb > 1L) sizeGb else if (sizeMb > 1) sizeMb else sizeKb
        val label = if (sizeGb > 1L) "GB" else if (sizeMb > 1) "MB" else "KB"

        return "$size $label"
    }
}

fun ItemDetail?.isAudio() = this?.mediaType == mediaTypeAudio

fun String?.isPdf() = this?.contains("pdf", true) ?: false

fun String?.isText() = this?.contains("txt", true) ?: false

fun String?.isMp3() = this?.contains("mp3", true) ?: false

fun File.isAudio() = this.format.isMp3()

fun File.isPdf() = format.isPdf()


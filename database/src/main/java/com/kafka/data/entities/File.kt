package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

const val mediaTypeText = "texts"
const val mediaTypeAudio = "audio"

@Entity
data class File(
    @PrimaryKey val fileId: String,
    val itemId: String,
    val itemTitle: String?,
    val size: Long?,
    val title: String?,
    val extension: String?,
    val creator: String?,
    val time: String?,
    val format: String?,
    val playbackUrl: String?,
    val readerUrl: String?,
    val coverImage: String?,
    val localUri: String? = null
): BaseEntity {
    companion object {
        val supportedFiles = listOf("pdf", "mp3", "epub", "wav", "txt")
    }

    val subtitle: String
        get() = listOf(extension, mapSize(), time).joinToString(" - ")

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
        val gap = 1_000
//        val size = this.toIntOrNull()
//            ?.run { this / 1000_000 }.toString()
//
//        val seconds = (this / 1000).toInt() % gap
//        val minutes = (this / (1000 * 60) % 60).toInt()
//        val hours = (this / (1000 * 60 * 60) % 24).toInt()
//        "${timeAddZeros(hours)}:${timeAddZeros(minutes, "0")}:${timeAddZeros(seconds, "00")}".apply {
//            return if (startsWith(":")) replaceFirst(":", "") else this
//        }
        return ""
    }
}

fun ItemDetail?.isAudio() = this?.mediaType == mediaTypeAudio

fun String?.isPdf() = this?.contains("pdf", true) ?: false

fun String?.isText() = this?.contains("txt", true) ?: false

fun String?.isMp3() = this?.contains("mp3", true) ?: false

fun File.isAudio() = this.format.isMp3()


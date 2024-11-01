package com.kafka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class File(
    @PrimaryKey val fileId: String,
    val itemId: String,
    val itemTitle: String?,
    val size: Long?,
    val name: String,
    val title: String,
    val extension: String?,
    val creator: String?,
    val time: String?,
    val format: String,
    val playbackUrl: String?,
    val readerUrl: String?,
    val downloadUrl: String?,
    val coverImage: String?,
    val localUri: String? = null,
) : BaseEntity {
    companion object {
        val audioExtensions = listOf("mp3", "wav", "m4a", "ogg", "aac", "flac")
        val textExtensions = listOf("pdf", "epub")
        val supportedExtensions = audioExtensions + textExtensions

        // extensions that show up in player, in order of preference
        val playableExtensions = listOf("mp3", "wav", "m4a", "ogg", "aac", "flac")
    }

    val duration: Long
        get() = time?.let { mapDuration(it) } ?: 0L

    private fun mapDuration(duration: String): Long {
        var durationInSeconds = 0L
        duration.split(":")
            .takeIf { it.size > 1 }
            ?.reversed()
            ?.forEachIndexed { index, time ->
                durationInSeconds += time.toInt() * (index * 60).coerceAtLeast(1)
            } ?: run {
            durationInSeconds = duration.toDouble().toLong()
        }

        return durationInSeconds
    }

    fun mapSize(): String {
        val step = 1000L
        val sizeKb = this.size?.run { this / step } ?: 0L
        val sizeMb = sizeKb / (step)
        val sizeGb = sizeMb / (step)
        val size = if (sizeGb > 1L) sizeGb else if (sizeMb > 1) sizeMb else sizeKb
        val label = if (sizeGb > 1L) "GB" else if (sizeMb > 1) "MB" else "KB"

        return "$size $label"
    }
}

fun String?.isTextExtension() = File.textExtensions.contains(this?.lowercase())
fun String?.isAudioExtension() = File.audioExtensions.contains(this?.lowercase())

fun File.isPlayable() = File.playableExtensions.contains(extension?.lowercase())
fun File.isAudio() = this.extension.isAudioExtension()
fun File.isText() = this.extension.isTextExtension()

fun File.nameWithoutExtension() = name.substringBeforeLast(".")

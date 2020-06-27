package com.kafka.data.entities

import com.kafka.data.extensions.formattedDuration
import com.kafka.data.query._mediaTypeText
import com.kafka.data.query._mediatypeAudio
import org.threeten.bp.Duration

data class File(
    val title: String?,
    val creator: String?,
    val time: Long,
    val format: String?,
    val playbackUrl: String?,
    val readerUrl: String?
)

fun ItemDetail?.isText() = this?.mediaType == _mediaTypeText
fun ItemDetail?.isAudio() = this?.mediaType == _mediatypeAudio

fun ItemDetail?.hasAudio() = this?.files?.firstOrNull { it.isMp3() } != null
fun ItemDetail?.hasText() = this?.files?.firstOrNull { it.isPdf() } != null

fun File.formattedDuration() = Duration.ofMillis(time).formattedDuration()

fun List<File>.filterMp3() = this.filter { it.isMp3() }

fun File.isPdf() = format?.isPdf() ?: false

fun File.isMp3() = format?.isMp3() ?: false

fun File.isCoverImage() = format?.contains("JPEG", true) ?: false ||
        format?.contains("Tile", true) ?: false

fun String?.isPdf() = this?.contains("pdf", true) ?: false

fun String?.isMp3() = this?.contains("mp3", true) ?: false

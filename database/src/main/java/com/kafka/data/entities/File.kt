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
    val size: String?,
    val title: String?,
    val extension: String?,
    val creator: String?,
    val time: Long,
    val format: String?,
    val playbackUrl: String?,
    val readerUrl: String?,
    val coverImage: String?,
    val localUri: String? = null
): BaseEntity {
    companion object {
        val supportedFiles = listOf("pdf", "mp3", "epub", "wav", "txt")
    }
}

fun ItemDetail?.isAudio() = this?.mediaType == mediaTypeAudio

fun String?.isPdf() = this?.contains("pdf", true) ?: false

fun String?.isText() = this?.contains("txt", true) ?: false

fun String?.isMp3() = this?.contains("mp3", true) ?: false

fun File.isAudio() = this.format.isMp3()


package com.kafka.data.model.item

import com.kafka.data.extensions.formattedDuration
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import org.threeten.bp.Duration
import org.threeten.bp.LocalDateTime

data class File(
    @Json(name = "album")
    val album: String,
    @Json(name = "artist")
    val artist: String,
    @Json(name = "bitrate")
    val bitrate: String,
    @Json(name = "btih")
    val btih: String,
    @Json(name = "crc32")
    val crc32: String,
    @Json(name = "genre")
    val creator: String,
    @Json(name = "format")
    val format: String,
    @Json(name = "genre")
    val genre: String,
    @Json(name = "height")
    val height: String,
    @Json(name = "length")
    val length: String,
    @Json(name = "md5")
    val md5: String,
    @Json(name = "mtime")
    val mtime: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "original")
    val original: String,
    @Json(name = "rotation")
    val rotation: String,
    @Json(name = "sha1")
    val sha1: String,
    @Json(name = "size")
    val size: String,
    @Json(name = "source")
    val source: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "track")
    val track: String,
    @Json(name = "width")
    val width: String
)

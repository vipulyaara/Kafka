package com.kafka.data.model.item

import kotlinx.serialization.SerialName

data class File(
    @SerialName("album")
    val album: String?,
    @SerialName("artist")
    val artist: String?,
    @SerialName("bitrate")
    val bitrate: String?,
    @SerialName("btih")
    val btih: String?,
    @SerialName("crc32")
    val crc32: String?,
    @SerialName("genre")
    val creator: String?,
    @SerialName("format")
    val format: String?,
    @SerialName("genre")
    val genre: String?,
    @SerialName("height")
    val height: String?,
    @SerialName("length")
    val length: String?,
    @SerialName("md5")
    val md5: String?,
    @SerialName("mtime")
    val mtime: String?,
    @SerialName("name")
    val name: String?,
    @SerialName("original")
    val original: String?,
    @SerialName("rotation")
    val rotation: String?,
    @SerialName("sha1")
    val sha1: String?,
    @SerialName("size")
    val size: String?,
    @SerialName("source")
    val source: String?,
    @SerialName("title")
    val title: String?,
    @SerialName("track")
    val track: String?,
    @SerialName("width")
    val width: String?
)

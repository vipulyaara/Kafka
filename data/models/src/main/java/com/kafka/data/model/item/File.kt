package com.kafka.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class File(
    @SerialName("album")
    val album: String? = null,
    @SerialName("artist")
    val artist: String? = null,
    @SerialName("bitrate")
    val bitrate: String? = null,
    @SerialName("btih")
    val btih: String? = null,
    @SerialName("crc32")
    val crc32: String? = null,
    @SerialName("creator")
    val creator: String? = null,
    @SerialName("format")
    val format: String? = null,
    @SerialName("genres")
    val genre: String? = null,
    @SerialName("height")
    val height: String? = null,
    @SerialName("length")
    val length: String? = null,
    @SerialName("md5")
    val md5: String? = null,
    @SerialName("mtime")
    val mtime: String? = null,
    @SerialName("name")
    val name: String,
    @SerialName("original")
    val original: String? = null,
    @SerialName("rotation")
    val rotation: String? = null,
    @SerialName("sha1")
    val sha1: String? = null,
    @SerialName("size")
    val size: String? = null,
    @SerialName("source")
    val source: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("track")
    val track: String? = null,
    @SerialName("width")
    val width: String? = null
) {
    // todo: improve file ids to make them unique
    // this will require a migration to new file ids which might be a breaking change
    val fileId: String
        get() = (name + format).hashCode().toString()
}

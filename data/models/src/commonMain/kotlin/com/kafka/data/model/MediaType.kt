package com.kafka.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MediaType(val value: String) {
    @SerialName("texts")
    Text("texts"),

    @SerialName("audio")
    Audio("audio");

    val isAudio
        get() = this == Audio

    companion object {
        fun from(value: String?) = MediaType.entries.firstOrNull { it.value == value } ?: Text

        val Default: MediaType = Text
    }
}

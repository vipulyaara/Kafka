package com.kafka.kms.data.models

data class LibrivoxAudiobook(
    val itemId: String,
    val title: String,
    val author: String,
    val description: String,
    val language: String,
    val coverImage: String?,
    val subjects: List<String>,
    val sections: List<LibrivoxSection>
)

data class LibrivoxSection(
    val title: String,
    val url: String,
    val duration: String,
    val durationInSeconds: Int
) 
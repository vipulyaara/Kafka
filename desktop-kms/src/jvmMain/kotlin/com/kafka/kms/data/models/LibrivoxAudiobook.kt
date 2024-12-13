package com.kafka.kms.data.models

data class LibrivoxAudiobook(
    val itemId: String,
    val title: String,
    val author: String,
    val description: String,
    val language: String,
    val coverImage: String?,
    val subjects: List<String>,
    val sections: List<LibrivoxSection>,
    val copyrightText: String = "This recording is in the public domain.",
    val isCopyrighted: Boolean = false,
    val sourceUrl: String,
    val translators: List<String> = emptyList()
)

data class LibrivoxSection(
    val id: String,
    val sectionNumber: String,
    val title: String,
    val listenUrl: String,
    val language: String,
    val playtime: String,
    val readers: List<Reader>
)

data class Reader(
    val readerId: String,
    val displayName: String
) 
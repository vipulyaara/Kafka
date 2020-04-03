package com.kafka.data.util

object NetworkConstants {
    fun bookDetailUrl(path: String, searchKeyword: String) = "https://librivox.org/api/feed/audiobooks/$path/$searchKeyword"

    const val baseUrl = "https://archive.org/"

    const val contentId = "languageIds"
}

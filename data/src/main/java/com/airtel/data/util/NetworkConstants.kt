package com.airtel.data.util

/**
 * Created by ajitp on 14/09/17.
 *
 */
object NetworkConstants {
    fun bookDetailUrl(path: String, searchKeyword: String) = "https://librivox.org/api/feed/audiobooks/$path/$searchKeyword"

    const val baseUrl = "https://archive.org/"

    const val contentId = "itemId"
}

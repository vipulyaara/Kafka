package com.kafka.player.model

import com.kafka.data.entities.ItemDetail

data class PlaybackItem(val id: String,
                        val bitrate: Int,
                        val duration: Long,
                        val resumePointInMilliSeconds: Long,
                        val contentUrl: String,
                        val requestCookieProperties: HashMap<String, String>?,
                        val shouldRetryOn403Error: Boolean,
                        val playingLanguage: String?,
                        val availableLanguages: List<String>?,
                        val subtitleUrls: HashMap<String, String>?,
                        val timeZone: String?,
                        val timeFormat: String?,
                        val arb: String?
) {
    var addToPlayList: Boolean = false

    constructor(url: String) : this("",
        0,
        0,
//        contentDetail.lastWatchedPositionInSeconds?.times(1000L) ?: 0,
        0,
//        contentDetail.playUrl ?: "",
        url,
        null,
        false,
        "",
        null,
        null,
        null,
        null,
        null)
}

fun getStreamingUrl(playId: String): String {
    val url = playId.split("|")
    return url[0]
}

//fun getTimeShiftUrl(playId: String, contentType: ContentType): String? {
//    return if (contentType == ContentType.LIVE) {
//        val url = playId.split("|")
//        when {
//            url.size > 1 -> url[1]
//            url.isNotEmpty() -> url[0]
//            else -> null
//        }
//    } else {
//        null
//    }
//}

package com.kafka.player.model

data class PlaybackItem(
    val id: String,
    val title: String,
    val imageUrl: String,
    val bitrate: Int,
    val duration: Long,
    val resumePointInMilliSeconds: Long,
    val contentUrl: String,
    val playingLanguage: String?,
    val availableLanguages: List<String>?
) {
    var addToPlayList: Boolean = false

    constructor(playbackUrl: String) : this(
        "", "", "", 0, 0, 0, playbackUrl, "", null
    )
}

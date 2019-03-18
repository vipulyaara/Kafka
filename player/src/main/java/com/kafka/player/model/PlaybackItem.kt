package com.kafka.player.model

/**
 * @author Vipul Kumar; dated 05/03/19.
 */
sealed class PlaybackItem {
    var id: String = ""
    var title: String? = null
    var stremingUrl: String? = null
    var imageUrl: String? = null
}

class Audio : PlaybackItem()

class Playlist : PlaybackItem() {
    var audios: List<Audio>? = null
}

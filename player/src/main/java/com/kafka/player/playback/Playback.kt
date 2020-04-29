package com.kafka.player.playback

interface Playback {

    var state: Int

    val isConnected: Boolean

    val isPlaying: Boolean

    val currentStreamPosition: Long

    val bufferedPosition: Long

    val duration: Long

    var currentMediaId: String

    var volume: Float

    fun getAudioSessionId():Int

    fun start()

    fun stop(notifyListeners: Boolean)

    fun updateLastKnownStreamPosition()

    fun play(mediaResource: MediaResource, isPlayWhenReady: Boolean)

    fun pause()

    fun seekTo(position: Long)

    fun onFastForward()

    fun onRewind()

    /**
     * 指定语速 refer 是否已当前速度为基数  multiple 倍率
     */
    fun onDerailleur(refer: Boolean, multiple: Float)

    interface Callback {
        fun onCompletion()

        fun onPlaybackStatusChanged(state: Int)

        fun onError(error: String)

        fun setCurrentMediaId(mediaId: String)
    }

    fun setCallback(callback: Callback)
}

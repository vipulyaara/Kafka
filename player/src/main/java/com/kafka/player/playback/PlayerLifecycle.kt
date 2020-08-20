package com.kafka.player.playback

interface PlayerLifecycle {
    fun onStop()
    fun onStart()
}

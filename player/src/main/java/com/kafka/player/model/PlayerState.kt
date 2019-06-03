package com.kafka.player.model

/**
 * Created by VipulKumar on 25/06/18.
 */
sealed class PlayerState {
    object Idle : PlayerState()
    data class Buffering(val playWhenReady: Boolean) : PlayerState()
    data class Playing(val playlistPosition: Int) : PlayerState()
    object Paused : PlayerState()
    data class Finished(val playlistPosition: Int) : PlayerState()
    data class Stopped(val wasPlaying: Boolean) : PlayerState()
    object PlaylistEnded : PlayerState()

    data class Error(
        val errorCode: Int,
        val errorMessage: String,
        val localizedMessage: String?,
        val isRecoverable: Boolean = false,
        val stackTrace: String? = null
    ) : PlayerState()
}

val noPlayerStateError =
    PlayerState.Error(1001, "Player State is not valid", "The player is not in a known state")
val streamingApiError =
    PlayerState.Error(1002, "Server Error", "Something went wrong. Please try again later")
val airtelOnlyError = PlayerState.Error(
    1003,
    "Playback paused due to non airtel user",
    "Switch to airtel to continue streaming"
)
val noNetworkError =
    PlayerState.Error(1004, "No network connection detected", "It seems you are offline")
val heartBeatError =
    PlayerState.Error(1005, "Playback paused due to multiple concurrent devices", "")
val invalidPlayUrlError =
    PlayerState.Error(1006, "Invalid play url", "Something went wrong. Please try again later")

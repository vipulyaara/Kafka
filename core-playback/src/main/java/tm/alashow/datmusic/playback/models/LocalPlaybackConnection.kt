package tm.alashow.datmusic.playback.models

import androidx.compose.runtime.staticCompositionLocalOf
import tm.alashow.datmusic.playback.PlaybackConnection

val LocalPlaybackConnection = staticCompositionLocalOf<PlaybackConnection> {
    error("No LocalPlaybackConnection provided")
}

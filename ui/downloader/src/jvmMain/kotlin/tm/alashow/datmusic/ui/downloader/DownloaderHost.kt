package tm.alashow.datmusic.ui.downloader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import tm.alashow.datmusic.downloader.Downloader

@Composable
actual fun DownloaderHost(
    downloader: Downloader,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalDownloader provides downloader) {
        content()
    }
}

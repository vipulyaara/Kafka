package tm.alashow.datmusic.ui.downloader

import androidx.compose.runtime.Composable
import tm.alashow.datmusic.downloader.Downloader

@Composable
expect fun DownloaderHost(
    downloader: Downloader,
    content: @Composable () -> Unit,
)

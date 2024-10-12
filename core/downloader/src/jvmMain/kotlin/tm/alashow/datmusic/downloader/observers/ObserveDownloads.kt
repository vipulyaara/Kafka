/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader.observers

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

actual class ObserveDownloads @Inject constructor() {
    actual fun execute(): Flow<DownloadItems> {
        return flowOf(DownloadItems())
    }
}

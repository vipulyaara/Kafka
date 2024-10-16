package com.kafka.ui.components.item

import androidx.compose.runtime.Composable
import com.kafka.data.feature.item.DownloadInfo

@Composable
expect fun DownloadStatusIcons(downloadInfo: DownloadInfo)
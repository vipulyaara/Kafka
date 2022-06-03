package org.kafka.item.files

import com.kafka.data.entities.File
import org.kafka.common.UiMessage

data class FilesViewState(
    val isDownloaded: Boolean = false,
    val files: List<File> = emptyList(),
    val isLoading: Boolean = false,
    val message: UiMessage? = null
)

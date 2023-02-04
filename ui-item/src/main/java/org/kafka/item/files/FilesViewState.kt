package org.kafka.item.files

import com.kafka.data.entities.File
import com.kafka.data.feature.item.ItemWithDownload
import org.kafka.common.UiMessage

data class FilesViewState(
    val downloads: List<ItemWithDownload> = emptyList(),
    val title: String = "",
    val files: List<File> = emptyList(),
    val isLoading: Boolean = false,
    val message: UiMessage? = null
)

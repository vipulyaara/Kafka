package org.kafka.item.files

import com.kafka.data.entities.File
import com.kafka.data.feature.item.ItemWithDownload
import org.kafka.common.snackbar.UiMessage

data class FilesViewState(
    val title: String = "",
    val downloads: List<ItemWithDownload> = emptyList(),
    val files: List<File> = emptyList(),
    val filteredFiles: List<File> = emptyList(),
    val actionLabels: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val message: UiMessage? = null
)

package org.kafka.item.files

import androidx.compose.runtime.Immutable
import com.kafka.data.entities.File
import com.kafka.data.feature.item.ItemWithDownload
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class FilesViewState(
    val title: String = "",
    val downloads: List<ItemWithDownload> = emptyList(),
    val files: List<File> = emptyList(),
    val filteredFiles: List<File> = emptyList(),
    val actionLabels: ImmutableList<String> = persistentListOf(),
    val isLoading: Boolean = false
)

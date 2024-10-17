package com.kafka.reader.epub

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.UiMessage
import com.kafka.reader.epub.domain.ParseEbook
import com.kafka.reader.epub.models.EpubBook
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Assisted
import javax.inject.Inject

class EpubReaderViewModel @Inject constructor(
    private val parseEbook: ParseEbook,
    private val snackbarManager: SnackbarManager,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val filePath = savedStateHandle.get<String>("filePath").orEmpty()
    val loading = parseEbook.inProgress.stateInDefault(viewModelScope, false)
    var ebook by mutableStateOf<EpubBook?>(null)

    init {
        loadEbook(filePath)
    }

    private fun loadEbook(path: String) {
        viewModelScope.launch {
            val result = parseEbook(path)
            result.onSuccess { ebook = it }
            result.onFailure { snackbarManager.addMessage(UiMessage.Error(it)) }
        }
    }
}

internal fun chunkText(text: String): List<String> {
    return text.splitToSequence("\n\n")
        .filter { it.isNotBlank() }
        .toList()
}

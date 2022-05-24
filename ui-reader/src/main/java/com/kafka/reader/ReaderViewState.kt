package com.kafka.reader

import androidx.compose.runtime.Immutable
import org.kafka.common.UiMessage

@Immutable
data class ReaderViewState(
    val readerUrl: String? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null
)

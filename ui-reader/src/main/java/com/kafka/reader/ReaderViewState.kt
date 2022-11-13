package com.kafka.reader

import android.net.Uri
import androidx.compose.runtime.Immutable
import org.kafka.common.UiMessage

@Immutable
data class ReaderViewState(
    val readerUrl: String? = null,
    val progress: Float = 0f,
    val uri: Uri? = null,
    val isLoading: Boolean = false,
    val message: UiMessage? = null,
)

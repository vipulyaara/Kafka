/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package org.kafka.ui.components.snackbar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.kafka.common.UiMessage
import org.kafka.common.UiMessageManager
import javax.inject.Inject

@HiltViewModel
internal class SnackbarMessagesHostViewModel @Inject constructor() : ViewModel() {
    val uiMessageManager = UiMessageManager()
    val messages = uiMessageManager.message

    suspend fun onSnackbarActionPerformed(message: UiMessage) = uiMessageManager.clearMessage(message.id)
    fun onSnackbarDismissed(message: UiMessage): () -> Unit = {  }
}

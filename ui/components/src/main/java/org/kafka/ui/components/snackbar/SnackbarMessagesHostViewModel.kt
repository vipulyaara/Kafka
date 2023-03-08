/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package org.kafka.ui.components.snackbar

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.SnackbarMessage
import javax.inject.Inject

@HiltViewModel
internal class SnackbarMessagesHostViewModel @Inject constructor(
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    val messages = snackbarManager.messages

    fun onSnackbarActionPerformed(message: SnackbarMessage<*>) =
        snackbarManager.onMessageActionPerformed(message)

    fun onSnackbarDismissed(message: SnackbarMessage<*>) =
        snackbarManager.onMessageDismissed(message)
}

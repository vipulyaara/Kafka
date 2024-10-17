package com.kafka.ui.components.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.common.snackbar.asString
import com.kafka.common.widgets.LocalSnackbarHostState
import kotlinx.coroutines.launch

@Composable
fun SnackbarMessagesHost(
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
    snackbarManager: SnackbarManager,
) {
    val coroutine = rememberCoroutineScope()

    // todo: kmp - check if CollectEvent is needed
    val messages by snackbarManager.messages.collectAsState(null)

    messages?.let {
        coroutine.launch {
            val message = it.message.asString()
            val actionLabel = it.action?.label?.asString()
            when (snackbarHostState.showSnackbar(message, actionLabel)) {
                SnackbarResult.ActionPerformed -> snackbarManager.onMessageActionPerformed(it)
                SnackbarResult.Dismissed -> snackbarManager.onMessageDismissed(it)
            }
        }
    }
}

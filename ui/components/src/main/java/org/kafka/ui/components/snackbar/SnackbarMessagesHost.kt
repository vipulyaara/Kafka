package org.kafka.ui.components.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import org.kafka.common.extensions.CollectEvent
import org.kafka.common.snackbar.SnackbarManager
import org.kafka.common.snackbar.asString
import org.kafka.common.widgets.LocalSnackbarHostState

@Composable
fun SnackbarMessagesHost(
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
    snackbarManager: SnackbarManager,
) {
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    CollectEvent(snackbarManager.messages) {
        coroutine.launch {
            val message = it.message.asString(context)
            val actionLabel = it.action?.label?.asString(context)
            when (snackbarHostState.showSnackbar(message, actionLabel)) {
                SnackbarResult.ActionPerformed -> snackbarManager.onMessageActionPerformed(it)
                SnackbarResult.Dismissed -> snackbarManager.onMessageDismissed(it)
            }
        }
    }
}

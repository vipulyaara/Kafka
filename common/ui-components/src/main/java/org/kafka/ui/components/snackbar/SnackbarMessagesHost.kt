package org.kafka.ui.components.snackbar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import org.kafka.common.extensions.CollectEvent
import org.kafka.common.widgets.LocalSnackbarHostState

@Composable
fun SnackbarMessagesHost() {
    SnackbarMessagesHost(viewModel = hiltViewModel())
}

@Composable
internal fun SnackbarMessagesHost(
    snackbarHostState: SnackbarHostState = LocalSnackbarHostState.current,
    viewModel: SnackbarMessagesHostViewModel
) {
    val coroutine = rememberCoroutineScope()
    CollectEvent(viewModel.messages) {
        coroutine.launch {
            it?.let {
                when (snackbarHostState.showSnackbar(it.message)) {
                    SnackbarResult.ActionPerformed -> viewModel.onSnackbarActionPerformed(it)
                    SnackbarResult.Dismissed -> viewModel.onSnackbarDismissed(it)
                }
            }
        }
    }
}

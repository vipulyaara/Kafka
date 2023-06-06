package org.kafka.common

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

fun <T> SavedStateHandle.getMutableStateFlow(
    key: String,
    defaultValue: T,
    scope: CoroutineScope,
): MutableStateFlow<T> {
    val state = MutableStateFlow(get<T>(key) ?: defaultValue)
    scope.launch {
        state.collectLatest { set(key, it) }
    }
    return state
}

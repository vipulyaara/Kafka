package org.kafka.common.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.kafka.base.domain.InvokeResponse
import org.kafka.base.errorLog
import kotlin.reflect.KProperty1

suspend fun <T> Flow<T>.collect(reducer: suspend (T) -> Unit) {
    collect { item -> reducer(item) }
}

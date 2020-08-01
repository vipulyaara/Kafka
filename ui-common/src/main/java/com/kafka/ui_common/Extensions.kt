package com.kafka.ui_common

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun EditText.onSearchIme(block: (String) -> Unit) = run {
    setOnEditorActionListener { v, actionId, event ->
    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
        block(v.text.toString())
        true
    }
    false
}
}
fun <T> delayFlow(timeout: Long, value: T): Flow<T> = flow {
    delay(timeout)
    emit(value)
}

package com.kafka.common.extensions

import androidx.compose.runtime.Composable

@Composable
actual fun getContext(): Any? = null

actual fun getActivity(context: Any?): Any? = null

actual fun updateApp(context: Any?) {
    //todo: kmp
}

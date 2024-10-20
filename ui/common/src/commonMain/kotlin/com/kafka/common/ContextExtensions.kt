package com.kafka.common

import androidx.compose.runtime.Composable

@Composable
expect fun getContext(): Any?

expect fun getActivity(context: Any?): Any?

expect fun updateApp(context: Any?)

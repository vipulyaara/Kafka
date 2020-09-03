package com.kafka.content

import com.kafka.ui_common.action.Action
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

fun <T : Action> Channel<T>.sendAction(action: T) = GlobalScope.launch {
    this@sendAction.send(action)
}

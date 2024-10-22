package com.kafka.navigation.deeplink

import com.kafka.navigation.deeplink.Config.BASE_URL
import com.kafka.navigation.graph.Screen

object DeepLinks {
    fun find(screen: Screen) = when (screen) {
        is Screen.ItemDetail -> "${BASE_URL}item/${screen.itemId}"
        else -> ""
    }
}

package com.kafka.content.compose

import androidx.activity.OnBackPressedDispatcher
import androidx.compose.foundation.lazy.ExperimentalLazyDsl
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.savedinstancestate.rememberSavedInstanceState
import com.kafka.ui.Navigator

@ExperimentalLazyDsl
@Composable
fun MainWindow(backDispatcher: OnBackPressedDispatcher) {
    val navigator: Navigator<Destination> = rememberSavedInstanceState(saver = Navigator.saver(backDispatcher)) {
        Navigator(Destination.Home, backDispatcher)
    }
    val actions = remember(navigator) { Actions(navigator) }

    when (val destination = navigator.current) {
        Destination.Home -> MainScreen(actions)
        is Destination.ItemDetail -> ItemDetail(itemId = destination.itemId)
    }
}

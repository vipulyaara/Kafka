package com.kafka.ui.player

import android.view.ViewGroup
import androidx.compose.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.kafka.ui.MaterialThemeFromAndroidTheme
import com.kafka.ui.home.HomepageAction
import com.kafka.ui.home.HomepageViewState
import com.kafka.ui.observe
import com.kafka.ui.setContentWithLifecycle

fun ViewGroup.composeHomepageScreen(
    lifecycleOwner: LifecycleOwner,
    state: LiveData<HomepageViewState>,
    actioner: (HomepageAction) -> Unit
): Any = setContentWithLifecycle(lifecycleOwner) {
    val viewState = observe(state)
    if (viewState != null) {
        MaterialThemeFromAndroidTheme(context) {
        }
    }

    @Composable
    fun MiniPlayer() {

    }
}

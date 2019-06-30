package com.kafka.user.feature.player

import android.os.Build
import androidx.annotation.RequiresApi
import com.airbnb.epoxy.TypedEpoxyController
import com.kafka.user.feature.home.HomepageViewState
import com.kafka.user.itemLoader
import com.kafka.user.itemPlayer

/**
 * @author Vipul Kumar; dated 2019-04-29.
 */
class PlayerController : TypedEpoxyController<PlayerViewState>() {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun buildModels(viewState: PlayerViewState) {
        if (viewState.isLoading) {
            buildLoadingState()
        } else {
            buildHomepageModels(viewState)
        }
    }

    private fun buildHomepageModels(viewState: PlayerViewState) {
        itemPlayer {
            id("player")
        }
    }

    private fun buildLoadingState() {
        itemLoader { id("loader") }
    }
}

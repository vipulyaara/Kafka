package com.kafka.content.ui.main

import android.app.Activity
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import com.data.base.launchObserve
import com.google.android.play.core.review.ReviewManagerFactory
import com.kafka.player.domain.CommandPlayer
import com.kafka.player.domain.ObservePlayingItem
import com.kafka.player.playback.PlayerLifecycle
import com.kafka.ui_common.base.ReduxViewModel

class MainViewModel @ViewModelInject constructor(
    private val playerLifecycle: PlayerLifecycle,
    observePlayingItem: ObservePlayingItem,
    commandPlayer: CommandPlayer
) : ReduxViewModel<MainViewState>(MainViewState()) {

    init {
        viewModelScope.launchObserve(observePlayingItem) {
            it.collectAndSetState { copy(playerCommand = { commandPlayer(it) }) }
        }

        observePlayingItem(Unit)
    }

    fun init() {
        playerLifecycle.onStart()
    }

    private fun showPlayStoreRatingDialog(context: Activity) {
        val manager = ReviewManagerFactory.create(context)

        val request = manager.requestReviewFlow()
        request.addOnCompleteListener { request ->
            if (request.isSuccessful) {
                // We got the ReviewInfo object
                val reviewInfo = request.result
                val flow = manager.launchReviewFlow(context, reviewInfo)
                flow.addOnCompleteListener { _ ->
                    // The flow has finished. The API does not indicate whether the user
                    // reviewed or not, or even whether the review dialog was shown. Thus, no
                    // matter the result, we continue our app flow.
                }
            }
        }

    }

}

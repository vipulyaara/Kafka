package com.kafka.user.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.kafka.user.R
import com.kafka.user.feature.player.PlayerService
import kotlinx.android.synthetic.main.widget_mini_player.view.*
import android.os.IBinder
import android.content.Intent
import android.os.Binder

/**
 * @author Vipul Kumar; dated 30/03/19.
 */
class PlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    val root = View.inflate(context, com.kafka.user.R.layout.widget_mini_player, this)!!

    val playerService = PlayerService()

    init {
       ivPlayPause.setOnClickListener { togglePlayPause() }
    }

    fun togglePlayPause() {
        if (playerService.isPlaying()) pause() else play()
    }

    fun play() {
        playerService.play()
    }

    fun pause() {
        playerService.pause()
    }
}

package com.kafka.user.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.kafka.data.data.config.kodeinInstance
import com.kafka.data.extensions.plusAssign
import com.kafka.data.util.AppRxSchedulers
import com.kafka.player.model.PlaybackItem
import com.kafka.player.model.PlayerState
import com.kafka.user.R
import com.kafka.user.extensions.logger
import com.kafka.user.player.Player
import com.kafka.user.player.dummyUrl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.widget_mini_player.view.*
import org.kodein.di.generic.instance

/**
 * @author Vipul Kumar; dated 30/03/19.
 */
class MiniPlayerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val schedulers by kodeinInstance.instance<AppRxSchedulers>()
    private val disposables = CompositeDisposable()

    val root: View = View.inflate(context, R.layout.widget_mini_player, this)

    init {
        disposables += Player.playerServiceConnection
            .observeOn(schedulers.io)
            .subscribe { initObservers() }

        ivPlayPause.setOnClickListener { togglePlayPause() }
    }

    private fun initObservers() {
        disposables += Player.playerStateObservable
            ?.observeOn(schedulers.io)
            ?.subscribe(::onPlayerStateChanged)
    }

    private fun onPlayerStateChanged(playerState: PlayerState) {
        logger.d("miniPlayer Player state $playerState")
        when (playerState) {
            is PlayerState.Playing -> {
                showPlayer()
                updateUi()
            }
            is PlayerState.Paused -> {
                updateUi()
            }
        }
    }

    private fun updateUi() {
        when (Player.playerState()) {
            is PlayerState.Playing -> {
                ivPlayPause.setImageResource(R.drawable.ic_pause_black_24dp)
            }
            is PlayerState.Paused -> {
                ivPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            }
        }
    }

    private fun showPlayer() {
        visibility = View.VISIBLE
    }

    fun togglePlayPause() {
        if (Player.isPlaying()) pause() else play()
    }

    fun play() {
        Player.play()
    }

    fun pause() {
        Player.pause()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        disposables.dispose()
    }
}

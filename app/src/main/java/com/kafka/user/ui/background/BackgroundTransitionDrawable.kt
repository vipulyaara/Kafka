package com.kafka.user.ui.background

import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.SystemClock
import android.util.Log

class BackgroundTransitionDrawable(layers: Array<Drawable>) : LayerDrawable(layers) {
    private var mAlpha = 255
    private var mAnimateDuration: Long = 0
    private var mAnimateStart: Long = 0
    private var mAnimating = false

    val backBuffer: Drawable
        get() = getDrawable(0)

    init {
        if (layers.size != 3) {
            throw IllegalArgumentException("Wrong number of layers: " + layers.size + " given, should have 3")
        }
    }

    fun animateFadeIn(durationMs: Long) {
        if (this.mAnimating) {
            Log.w(TAG, "animateFadeIn called while the previous animation is still running.")
        }
        this.mAnimateStart = SystemClock.uptimeMillis()
        this.mAnimateDuration = durationMs
        this.mAnimating = true
        flipBuffers()
    }

    override fun draw(canvas: Canvas) {
        if (this.mAnimating) {
            val time = SystemClock.uptimeMillis() - this.mAnimateStart
            if (time >= this.mAnimateDuration) {
                this.mAnimating = false
                this.mAlpha = 255
            } else {
                this.mAlpha = (255 * time / this.mAnimateDuration).toInt()
            }
        }
        if (this.mAlpha != 255) {
            getDrawable(1).alpha = 255
            getDrawable(1).draw(canvas)
        }
        if (this.mAlpha != 0) {
            getDrawable(2).alpha = this.mAlpha
            getDrawable(2).draw(canvas)
        }
        if (this.mAnimating) {
            invalidateSelf()
        }
    }

    private fun flipBuffers() {
        val back = getDrawable(0)
        setDrawable(0, getDrawable(1))
        setDrawable(1, getDrawable(2))
        setDrawable(2, back)
        invalidateSelf()
    }

    companion object {
        private val TAG = "BackgroundTransition"
    }
}

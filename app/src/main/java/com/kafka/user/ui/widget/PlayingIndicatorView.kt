package com.kafka.user.ui.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.kafka.user.R

class PlayingIndicatorView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val mAnimator: ValueAnimator
    private val mBarSeparationPx: Int
    private val mBarWidthPx: Int
    private val mDrawRect = Rect()
    private val mPaint: Paint
    var playing: Boolean = false
    private var mProgress: Float = 0.toFloat()

    init {
        val res = context.resources
        this.mBarWidthPx = 100
        this.mBarSeparationPx = 12
        this.mAnimator = ValueAnimator()
        this.mAnimator.interpolator = LinearInterpolator()
        this.mAnimator.repeatCount = -1
        this.mAnimator.duration = 100000000
        this.mAnimator.setFloatValues(0.0f, (this.mAnimator.duration / 80).toFloat())
        this.mAnimator.addUpdateListener { animation ->
            this@PlayingIndicatorView.mProgress = (animation.animatedValue as Float).toFloat()
            this@PlayingIndicatorView.invalidate()
        }
        this.mPaint = Paint()
        this.mPaint.color = -1
        setLayerType(1, null)
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = this.mBarWidthPx * 3 + this.mBarSeparationPx * 2
        setMeasuredDimension(width, width)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        startAnimationIfVisible()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        stopAnimation()
    }

    fun stopAnimation() {
        this.mAnimator.cancel()
        postInvalidate()
    }

    fun setColorFilter(colorFilter: ColorFilter) {
        this.mPaint.colorFilter = colorFilter
    }

    fun startAnimationIfVisible() {
        if (visibility == 0) {
            this.mAnimator.start()
            postInvalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        drawRectangles(canvas)
    }

    private fun drawRectangles(canvas: Canvas) {
        for (barIndex in 0..2) {
            this.mDrawRect.left = (this.mBarWidthPx + this.mBarSeparationPx) * barIndex
            this.mDrawRect.right = this.mDrawRect.left + this.mBarWidthPx
            this.mDrawRect.bottom = height
            this.mDrawRect.top =
                (height.toFloat() * (15.0f - if (this.playing) linearlyInterpolateWithWrapping(
                    this.mProgress,
                    LEVELS[barIndex]
                ) else 0.5f) / 15.0f).toInt()
            canvas.drawRect(this.mDrawRect, this.mPaint)
        }
    }

    companion object {
        private val LEVELS = arrayOf(
            intArrayOf(
                5,
                3,
                5,
                7,
                9,
                10,
                11,
                12,
                11,
                12,
                10,
                8,
                7,
                4,
                2,
                4,
                6,
                7,
                9,
                11,
                9,
                7,
                5,
                3,
                5,
                8,
                5,
                3,
                4
            ),
            intArrayOf(
                12,
                11,
                10,
                11,
                12,
                11,
                9,
                7,
                9,
                11,
                12,
                10,
                8,
                10,
                12,
                11,
                9,
                5,
                3,
                5,
                8,
                10,
                12,
                10,
                9,
                8
            ),
            intArrayOf(
                8,
                9,
                10,
                12,
                11,
                9,
                7,
                5,
                7,
                8,
                9,
                12,
                11,
                12,
                9,
                7,
                9,
                11,
                12,
                10,
                8,
                9,
                7,
                5,
                3
            )
        )

        private fun linearlyInterpolateWithWrapping(position: Float, array: IntArray): Float {
            val positionRoundedDown = position.toInt()
            val beforeIndex = positionRoundedDown % array.size
            val weight = position - positionRoundedDown.toFloat()
            return array[beforeIndex].toFloat() * (1.0f - weight) + array[(beforeIndex + 1) % array.size].toFloat() * weight
        }
    }
}

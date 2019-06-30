package com.kafka.user.ui.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.kafka.user.ui.graphics.ImageLoadingColorMatrix
import kotlin.math.roundToLong

private val fastOutSlowInInterpolator = FastOutSlowInInterpolator()

fun saturateDrawableAnimator(current: Drawable, view: View): Animator {
    view.setHasTransientState(true)
    val cm = ImageLoadingColorMatrix()

    val duration = 1500L

    val satAnim = ObjectAnimator.ofFloat(cm, ImageLoadingColorMatrix.PROP_SATURATION, 0f, 1f)
    satAnim.duration = duration
    satAnim.interpolator = fastOutSlowInInterpolator
    satAnim.addUpdateListener { current.colorFilter = ColorMatrixColorFilter(cm) }

    val alphaAnim = ObjectAnimator.ofFloat(cm, ImageLoadingColorMatrix.PROP_ALPHA, 0f, 1f)
    alphaAnim.duration = duration / 2
    alphaAnim.interpolator = fastOutSlowInInterpolator

    val darkenAnim = ObjectAnimator.ofFloat(cm, ImageLoadingColorMatrix.PROP_DARKEN, 0f, 1f)
    darkenAnim.duration = (duration * 0.75f).roundToLong()
    darkenAnim.interpolator = fastOutSlowInInterpolator

    val set = AnimatorSet()
    set.playTogether(satAnim, alphaAnim, darkenAnim)
    set.doOnEnd {
        current.clearColorFilter()
        view.setHasTransientState(false)
    }
    return set
}

/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kafka.user.ui.animation

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.ColorMatrixColorFilter
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.animation.doOnEnd
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.kafka.user.ui.graphics.ImageLoadingColorMatrix
import kotlin.math.roundToLong

private val fastOutSlowInInterpolator = FastOutSlowInInterpolator()

fun saturateDrawableAnimator(current: Drawable, view: View): Animator {
    current.mutate()
    view.setHasTransientState(true)
    val cm = ImageLoadingColorMatrix()

    val satAnim = ObjectAnimator.ofFloat(cm, ImageLoadingColorMatrix.PROP_SATURATION, 0f, 1f)
    satAnim.duration = SATURATION_ANIMATION_DURATION
    satAnim.addUpdateListener {
        current.colorFilter = ColorMatrixColorFilter(cm)
    }

    val alphaAnim = ObjectAnimator.ofFloat(cm, ImageLoadingColorMatrix.PROP_ALPHA, 0f, 1f)
    alphaAnim.duration = SATURATION_ANIMATION_DURATION / 2

    val darkenAnim = ObjectAnimator.ofFloat(cm, ImageLoadingColorMatrix.PROP_BRIGHTNESS, 0.8f, 1f)
    darkenAnim.duration = (SATURATION_ANIMATION_DURATION * 0.75f).roundToLong()

    return AnimatorSet().apply {
        playTogether(satAnim, alphaAnim, darkenAnim)
        interpolator = fastOutSlowInInterpolator
        doOnEnd {
            current.clearColorFilter()
            view.setHasTransientState(false)
        }
    }
}

private const val SATURATION_ANIMATION_DURATION = 1000L


fun View?.animationIn(animation: Int, delayTime: Int, context: Context) {
    val handler = Handler()
    handler.postDelayed({
        val inAnimation = AnimationUtils.loadAnimation(
            context.applicationContext, animation
        )
        this?.animation = inAnimation
        this?.visibility = View.VISIBLE
    }, delayTime.toLong())
}

fun View?.animationOut(
    animation: Int,
    delayTime: Int,
    isViewGone: Boolean,
    context: Context
) {
    this?.visibility = View.VISIBLE
    val handler = Handler()
    handler.postDelayed({
        val outAnimation = AnimationUtils.loadAnimation(
            context.applicationContext, animation
        )
        this?.animation = outAnimation
        if (isViewGone)
            this?.visibility = View.GONE
        else
            this?.visibility = View.INVISIBLE
    }, delayTime.toLong())
}
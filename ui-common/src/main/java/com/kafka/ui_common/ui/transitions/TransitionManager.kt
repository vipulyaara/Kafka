package com.kafka.ui_common.ui.transitions

import android.graphics.Color
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform

object TransitionManager {
    fun getMaterialContainerTransform() = MaterialContainerTransform().apply {
        fadeMode = MaterialContainerTransform.FADE_MODE_IN
        interpolator = FastOutSlowInInterpolator()
        scrimColor = Color.TRANSPARENT
        setPathMotion(MaterialArcMotion())
        startElevation = 1f
        endElevation = 1f
        duration = 300
    }
}


package com.kafka.user.ui.graphics

import android.graphics.drawable.Drawable
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.request.transition.NoTransition
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.request.transition.TransitionFactory
import com.kafka.user.ui.animation.saturateDrawableAnimator

class SaturationTransitionFactory : TransitionFactory<Drawable> {
    override fun build(dataSource: DataSource, isFirstResource: Boolean): Transition<Drawable> {
        return if (isFirstResource && dataSource != DataSource.MEMORY_CACHE) {
            // Only start the transition if this is not a recent load. We approximate that by
            // checking if the image is from the memory cache
            SaturationTransition()
        } else {
            NoTransition<Drawable>()
        }
    }
}

internal class SaturationTransition : Transition<Drawable> {
    override fun transition(current: Drawable, adapter: Transition.ViewAdapter): Boolean {
        saturateDrawableAnimator(current, adapter.view).also {
            it.start()
        }
        // We want Glide to still set the drawable
        return false
    }
}

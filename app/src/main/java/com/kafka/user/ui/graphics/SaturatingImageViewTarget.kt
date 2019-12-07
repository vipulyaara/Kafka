package com.kafka.user.ui.graphics

import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import coil.decode.DataSource
import coil.request.Request
import coil.target.PoolableViewTarget
import coil.target.Target
import com.kafka.user.ui.animation.saturateDrawableAnimator

/**
 * A [Target], which handles setting images on an [ImageView].
 */
class SaturatingImageViewTarget(
    override val view: ImageView
) : PoolableViewTarget<ImageView>, DefaultLifecycleObserver, Request.Listener {
    private var isStarted = false

    override fun onStart(placeholder: Drawable?) = setDrawable(placeholder)

    override fun onSuccess(result: Drawable) = setDrawable(result)

    override fun onSuccess(data: Any, source: DataSource) {
        // This is called after onSuccess(Drawable) above, so we can assume the image has
        // already been set
        if ((source == DataSource.DISK || source == DataSource.NETWORK) && view.drawable != null) {
            saturateDrawableAnimator(view.drawable, view).start()
        }
    }

    override fun onError(error: Drawable?) = setDrawable(error)

    override fun onClear() = setDrawable(null)

    override fun onStart(owner: LifecycleOwner) {
        isStarted = true
        updateAnimation()
    }

    override fun onStop(owner: LifecycleOwner) {
        isStarted = false
        updateAnimation()
    }

    private fun setDrawable(drawable: Drawable?) {
        (view.drawable as? Animatable)?.stop()
        view.setImageDrawable(drawable)
        updateAnimation()
    }

    private fun updateAnimation() {
        val animatable = view.drawable as? Animatable ?: return
        if (isStarted) animatable.start() else animatable.stop()
    }
}

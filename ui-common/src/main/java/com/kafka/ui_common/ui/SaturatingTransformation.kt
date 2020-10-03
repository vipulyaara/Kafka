package com.kafka.ui_common.ui

import androidx.core.animation.doOnEnd
import coil.annotation.ExperimentalCoilApi
import coil.decode.DataSource
import coil.request.ErrorResult
import coil.request.ImageResult
import coil.request.SuccessResult
import coil.transition.Transition
import coil.transition.TransitionTarget
import com.kafka.ui_common.ui.animations.SATURATION_ANIMATION_DURATION
import com.kafka.ui_common.ui.animations.saturateDrawableAnimator
import kotlinx.coroutines.suspendCancellableCoroutine

/** A [Transition] that saturates and fades in the new drawable on load */
@ExperimentalCoilApi
class SaturatingTransformation(
    private val durationMillis: Long = SATURATION_ANIMATION_DURATION
) : Transition {
    init {
        require(durationMillis > 0) { "durationMillis must be > 0." }
    }

    override suspend fun transition(
        target: TransitionTarget,
        result: ImageResult
    ) {
        // Don't animate if the request was fulfilled by the memory cache.
        if (result is SuccessResult && result.metadata.dataSource == DataSource.MEMORY_CACHE) {
            target.onSuccess(result.drawable)
            return
        }

        // Animate the drawable and suspend until the animation is completes.
        suspendCancellableCoroutine<Unit> { continuation ->
            when (result) {
                is SuccessResult -> {
                    val animator = saturateDrawableAnimator(
                        result.drawable,
                        durationMillis, target.view
                    )
                    animator.doOnEnd {
                        continuation.resume(Unit) { animator.cancel() }
                    }
                    animator.start()

                    continuation.invokeOnCancellation { animator.cancel() }
                    target.onSuccess(result.drawable)
                }
                is ErrorResult -> target.onError(result.drawable)
            }
        }
    }
}

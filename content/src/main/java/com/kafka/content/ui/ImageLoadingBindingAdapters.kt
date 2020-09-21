package com.kafka.content.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.annotation.ExperimentalCoilApi
import coil.loadAny
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
import com.kafka.content.R
import com.kafka.ui_common.ui.SaturatingTransformation

@BindingAdapter(
    "image",
    "imageSaturateOnLoad",
    "imageCornerRadius",
    requireAll = false
)
@ExperimentalCoilApi
fun ImageView.loadImage(
    oldImage: String?,
    oldSaturateOnLoad: Boolean?,
    oldCornerRadius: Float,
    image: String?,
    saturateOnLoad: Boolean?,
    cornerRadius: Float
) {
    if (oldImage == image &&
        oldSaturateOnLoad == saturateOnLoad &&
        oldCornerRadius == cornerRadius
    ) return

    loadAny(image) {
        scale(Scale.FILL)
        transition(SaturatingTransformation())

        val transformations = ArrayList<Transformation>()
        if (cornerRadius > 0) {
            transformations += RoundedCornersTransformation(cornerRadius)
        }

        transformations(transformations)
    }
}

@BindingAdapter("image")
fun ImageView.loadImage(image: Int?) {
    loadAny(image)
}

@BindingAdapter("playIcon")
fun ImageView.playIcon(isPlaying: Boolean?) {
    val icon = when (isPlaying) {
        null -> 0
        true -> R.drawable.ic_pause
        false -> R.drawable.ic_play
    }
    setImageResource(icon)
}

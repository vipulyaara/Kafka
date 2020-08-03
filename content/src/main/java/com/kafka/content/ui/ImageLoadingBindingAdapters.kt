package com.kafka.content.ui

import android.view.View
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import coil.annotation.ExperimentalCoilApi
import coil.api.loadAny
import coil.transform.RoundedCornersTransformation
import coil.transform.Transformation
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
        transition(SaturatingTransformation())

        val transformations = ArrayList<Transformation>()
        if (cornerRadius > 0) {
            transformations += RoundedCornersTransformation(cornerRadius)
        }

        transformations(transformations)
    }
}

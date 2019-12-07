package com.kafka.user.ui.binding

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.loadAny
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.kafka.user.R
import com.kafka.user.ui.graphics.SaturatingImageViewTarget
import kotlin.random.Random

/**
 * @author Vipul Kumar; dated 22/01/19.
 */

val placeholderArray = arrayOf(
    R.color.placeholder_1, R.color.placeholder_2, R.color.placeholder_3,
    R.color.placeholder_4, R.color.placeholder_5, R.color.placeholder_6,
    R.color.placeholder_7, R.color.placeholder_8, R.color.placeholder_9,
    R.color.placeholder_10
)

fun getRandomPlaceholderColor() = placeholderArray.let {
    it[Random.nextInt(it.size)]
}

@BindingAdapter(value = ["visibleGone"])
fun View.visibleGone(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter(
    "image",
    "imageSaturateOnLoad",
    "imageCornerRadius",
    "imageCircular",
    requireAll = false
)
fun ImageView.loadImage(
    oldImage: String?,
    oldSaturateOnLoad: Boolean?,
    oldCornerRadius: Float,
    oldCircular: Boolean?,
    image: String?,
    saturateOnLoad: Boolean?,
    cornerRadius: Float,
    circular: Boolean?
) {
    if (oldImage == image &&
        oldSaturateOnLoad == saturateOnLoad &&
        oldCornerRadius == cornerRadius) return

    loadAny(image) {
        crossfade(true)
        placeholder(getRandomPlaceholderColor())
        if (saturateOnLoad == null || saturateOnLoad == true) {
            val saturatingTarget =
                SaturatingImageViewTarget(this@loadImage)
            target(saturatingTarget)
            listener(saturatingTarget)
        }
        if (circular == true) {
            transformations(CircleCropTransformation())
        }
        if (cornerRadius > 0) {
            transformations(RoundedCornersTransformation(cornerRadius))
        }
    }
}
@BindingAdapter(
    "imageResource",
    "imageCircular",
    requireAll = false
)
fun ImageView.loadImage(
    imageResource: Drawable?,
    imageCircular: Boolean?
) {

    loadAny(imageResource) {
        crossfade(true)
        placeholder(getRandomPlaceholderColor())
        if (imageCircular == true) {
            transformations(CircleCropTransformation())
        }
    }
}

@BindingAdapter("android:src")
fun bindImage(imageView: ImageView?, resource: Int) {
    imageView?.setImageResource(resource)
}

@BindingAdapter("focusListener")
fun setFocusListener(view: View, focusChangeListener: View.OnFocusChangeListener) {
    view.onFocusChangeListener = focusChangeListener
}

@BindingAdapter("gradientBackground")
fun gradientBackground(view: View, enable: Boolean) {
    if (enable) {
        view.setBackgroundColor(Color.parseColor("#000000"))
    }
}

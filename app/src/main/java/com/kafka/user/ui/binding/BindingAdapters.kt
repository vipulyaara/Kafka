package com.kafka.user.ui.binding

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kafka.user.ui.GlideApp
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.kafka.user.ui.GradientGenerator

/**
 * @author Vipul Kumar; dated 22/01/19.
 */

@BindingAdapter(value = ["visibleGone"])
fun View.visibleGone(visible: Boolean) {
    visibility = if (visible) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("android:src")
fun setImageResource(imageView: ImageView, resource: Int) {
    imageView.setImageResource(resource)
}

@BindingAdapter("srcRounded")
fun setImageResourceRounded(imageView: ImageView, resource: Int) {
    GlideApp.with(imageView).load(resource)
        .apply(RequestOptions.circleCropTransform())
        .into(imageView)
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

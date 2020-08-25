package com.kafka.content.ui

import android.view.View
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.data.base.extensions.debug
import com.google.android.material.slider.Slider
import com.kafka.content.R
import com.kafka.ui_common.extensions.onSearchIme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@BindingAdapter("visibleGone")
fun View.visibleGone(visibleGone: Boolean) {
    isVisible = visibleGone
}

@BindingAdapter("visibleInvisible")
fun View.visibleInvisible(visibleInvisible: Boolean) {
    isInvisible = !visibleInvisible
}

@BindingAdapter("onImeAction")
fun EditText.onImeAction(actionListener: ActionListener) {
    onSearchIme { actionListener.onAction(this) }
}

@BindingAdapter("seekFlow")
fun Slider.moveSeekBar(seekFlow: Flow<Int>?) {
    GlobalScope.launch {
        seekFlow?.collect {
            debug { "Progress is $it" }
            this@moveSeekBar.value = it.toFloat()
        }
    }
}

@BindingAdapter("iconIfPlaying")
fun LottieAnimationView.iconIfPlaying(isPlaying: Boolean) {
    if (isPlaying) {
        setAnimation(R.raw.playing)
    } else {
        setImageResource(R.drawable.ic_disc_padding)
    }
}

interface ActionListener {
    fun onAction(editText: EditText)
}

package com.kafka.content.ui

import android.view.View
import android.widget.EditText
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.kafka.ui_common.onSearchIme

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

interface ActionListener {
    fun onAction(editText: EditText)
}

package com.kafka.content.ui

import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
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
fun SeekBar.moveSeekBar(seekFlow: Flow<Int>?) {
    GlobalScope.launch { seekFlow?.collect { progress = it } }
}

interface ActionListener {
    fun onAction(editText: EditText)
}

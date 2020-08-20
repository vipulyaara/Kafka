package com.kafka.ui_common.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.kafka.ui_common.R
import com.kafka.ui_common.ui.widget.toast

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    toast(message ?: "", length).show()
}

fun View.showSnackbar(message: String?, length: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, message ?: "", length).also {
        it.setBackgroundTint(context!!.getColor(R.color.colorAccent))
    }.show()
}

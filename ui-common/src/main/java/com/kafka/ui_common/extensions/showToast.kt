package com.kafka.ui_common.extensions

import android.content.Context
import android.widget.Toast
import com.kafka.ui_common.ui.widget.toast

fun Context.showToast(message: String?, length: Int = Toast.LENGTH_SHORT) {
    toast(message ?: "", length).show()
}

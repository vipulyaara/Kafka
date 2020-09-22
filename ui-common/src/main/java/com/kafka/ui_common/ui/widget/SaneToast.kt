package com.kafka.ui_common.ui.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.kafka.ui_common.R

/**
 * A Toast built with custom UI and Singleton pattern.
 */

private var SANE_TOAST: Toast? = null

private fun toast(context: Context): Toast {
    if (SANE_TOAST == null) {
        SANE_TOAST = Toast(context)
    }
    return SANE_TOAST as Toast
}

fun Context.toast(message: String): Toast {
    val inflater = LayoutInflater.from(this)
    val layout = inflater.inflate(R.layout.view_toast, null)
    val text = layout.findViewById<TextView>(R.id.tv_toast_msg)
    text.text = message

    // Prevent creating multiple toasts at a time
    val toast = toast(this)
    toast.view = layout
    toast.duration = Toast.LENGTH_SHORT
    return toast
}

fun Context.toast(message: String, length: Int): Toast {
    val toast = toast(message)
    toast.duration = length
    return toast
}

fun Context.toast(message: String, iconResource: Int?): Toast {
    val toast = toast(message)
    val view = toast.view
    val imageView = view?.findViewById<ImageView>(R.id.iv_toast_icon)
    imageView?.visibility = View.VISIBLE
    imageView?.setImageResource(iconResource ?: 0)
    toast.view = view
    return toast
}

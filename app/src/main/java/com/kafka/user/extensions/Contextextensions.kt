package com.kafka.user.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.widget.Toast
import androidx.core.content.ContextCompat

/**
 * @author Vipul Kumar; dated 04/04/19.
 */
fun <Service> ContextWrapper.getService(name: String) = getSystemService(name) as Service

fun Context.getColorCompat(resource: Int) = ContextCompat.getColor(this, resource)

fun Context.resolveActivity(): Activity {
    if (this is Activity) {
        return this
    }
    if (this is ContextWrapper) {
        return baseContext.resolveActivity()
    }
    throw UnsupportedOperationException("Given context was not an activity! Is a $this")
}

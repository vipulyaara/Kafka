package com.kafka.user.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.data.base.extensions.debug

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

fun Context.openPdf(url: String) {
    debug { "Opening pdf for $url" }

    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(url.toUri(), "application/pdf")
    intent.putExtra("page", 23)
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    startActivity(intent)
}

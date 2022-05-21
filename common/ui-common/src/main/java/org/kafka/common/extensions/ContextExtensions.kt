package org.kafka.common.extensions

import android.content.Context
import android.provider.Settings

fun Context.androidId() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

fun <T> Context.systemService(name: String): T {
    return getSystemService(name) as T
}

package com.kafka.ui_common.extensions

import android.content.Context
import android.content.Intent

fun Context.shareText(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, "Share via")
    startActivity(shareIntent)
}

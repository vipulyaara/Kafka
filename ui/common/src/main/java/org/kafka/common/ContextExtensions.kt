package org.kafka.common

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.shareText(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

fun Context.toast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Context.goToPlayStore() {
    val releasePackageName = packageName
        .removeSuffix(".debug")
        .removeSuffix(".rc")

    val marketUri = Uri.parse("market://details?id=$releasePackageName")
    val playStoreUri =
        Uri.parse("https://play.google.com/store/apps/details?id=$releasePackageName")

    try {
        startActivity(Intent(Intent.ACTION_VIEW, marketUri))
    } catch (e: ActivityNotFoundException) {
        startActivity(Intent(Intent.ACTION_VIEW, playStoreUri))
    }
}

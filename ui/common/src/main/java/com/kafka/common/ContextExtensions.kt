package com.kafka.common

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun getContext(): Any? = LocalContext.current

fun Context.shareText(text: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, null)
    startActivity(shareIntent)
}

actual fun getActivity(context: Any?): Any? = when (context) {
    is Activity -> context
    is ContextWrapper -> context.baseContext.getActivity()
    else -> null
}

fun Context.getActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

actual fun updateApp(context: Any?) {
    val ctx = context as Context
    val releasePackageName = ctx
        .packageName
        .removeSuffix(".debug")
        .removeSuffix(".rc")

    val marketUri = Uri.parse("market://details?id=$releasePackageName")
    val playStoreUri =
        Uri.parse("https://play.google.com/store/apps/details?id=$releasePackageName")

    try {
        ctx.startActivity(Intent(Intent.ACTION_VIEW, marketUri))
    } catch (e: ActivityNotFoundException) {
        ctx.startActivity(Intent(Intent.ACTION_VIEW, playStoreUri))
    }
}

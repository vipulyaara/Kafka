package com.kafka.user.ui

import android.app.ActivityManager
import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.kafka.user.BuildConfig

/**
 * This module is used to provide global configuration for Glide e.g. Log level, Disk Strategy etc.
 * Please keep in mind that all Glide modules are to be kept in Proguard.
 * */
@GlideModule
class GlobalGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val options = RequestOptions()
            .format(if (am.isLowRamDevice) DecodeFormat.PREFER_RGB_565 else DecodeFormat.PREFER_ARGB_8888)

        builder.setDefaultRequestOptions(options)
        if (BuildConfig.DEBUG) {
            builder.setLogLevel(Log.VERBOSE)
        }
    }

    override fun isManifestParsingEnabled() = false
}

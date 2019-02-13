package com.airtel.kafkapp.ui

import android.content.Context
import android.util.Log
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions

/**
 * This module is used to provide global configuration for Glide e.g. Log level, Disk Strategy etc.
 * Please keep in mind that all Glide modules are to be kept in Proguard.
 * */
@GlideModule
class GlobalGlideModule : AppGlideModule() {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        builder.apply {
            setLogLevel(Log.VERBOSE)
            setDefaultRequestOptions(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            )
        }
    }
}

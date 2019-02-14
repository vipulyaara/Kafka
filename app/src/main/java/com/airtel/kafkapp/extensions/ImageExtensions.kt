package com.airtel.kafkapp.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.migration.Migration

/**
 * Created by VipulKumar on 21/09/18.
 */

inline fun ImageView.loadImage(
    requestManager: RequestManager = Glide.with(this),
    func:
    RequestManager.() -> RequestBuilder<Drawable>
) {
    this.context?.let {
        requestManager
            .func()
            .into(this)
    }
}

@BindingAdapter(value = ["srcUrl"])
fun srcUrl(
    view: ImageView,
    srcUrl: String?
) {
    srcUrl?.let {
        view.loadImage { load(srcUrl) }
    }
}

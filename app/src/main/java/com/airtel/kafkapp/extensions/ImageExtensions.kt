package com.airtel.kafkapp.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.airtel.kafkapp.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions

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
        logger.d("Image $srcUrl")
        view.loadImage {
            load(srcUrl).apply(
                RequestOptions().placeholder(R.drawable.ic_linked_camera_black_24dp)
                    .centerCrop()
            )
        }
    }
}

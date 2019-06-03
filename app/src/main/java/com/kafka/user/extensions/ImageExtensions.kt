package com.kafka.user.extensions

import android.graphics.Bitmap
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.zomato.photofilters.geometry.Point
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ToneCurveSubFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by VipulKumar on 21/09/18.
 */

inline fun ImageView.loadImage(
    requestManager: RequestManager = Glide.with(this),
    func:
    RequestManager.() -> RequestBuilder<Bitmap>
) {
    this.context?.let {
        requestManager
            .func()
            .listener(object : RequestListener<Bitmap> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    isFirstResource: Boolean
                ): Boolean {
                    return true
                }

                override fun onResourceReady(
                    resource: Bitmap?,
                    model: Any?,
                    target: Target<Bitmap>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    GlobalScope.launch {
                        val image = withContext(Dispatchers.Default) { resource }
                        withContext(Dispatchers.Main) { this@loadImage.setImageBitmap(image) }
                    }

                    return true
                }
            })
            .into(this)
    }
}

fun Bitmap.applyFilter(): Bitmap? {
    val myFilter = Filter()
    val rgbKnots: Array<Point> = arrayOf(
        Point(0f, 0f),
        Point(175f, 139f),
        Point(255f, 255f)
    )

    myFilter.addSubFilter(ToneCurveSubFilter(rgbKnots, null, null, null))
    return myFilter.processFilter(this)
}

fun Bitmap.applyColorFilter(): Bitmap? {
    val myFilter = Filter()
    myFilter.addSubFilter(ColorOverlaySubFilter(100, .2f, .2f, .0f))
    return myFilter.processFilter(this)
}

@BindingAdapter(value = ["srcUrl"])
fun srcUrl(
    view: ImageView,
    srcUrl: String?
) {
    srcUrl?.let {
        view.loadImage {
            asBitmap().load(srcUrl).apply(
                RequestOptions().placeholder(com.kafka.user.R.drawable.ic_linked_camera_black_24dp)
                    .centerCrop()
            )
        }
    }
}

package tm.alashow.datmusic.playback

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Precision

fun isOreo() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

fun <T> List<T>.swap(from: Int, to: Int): List<T> {
    val new = toMutableList()
    val element = new.removeAt(from)
    new.add(to, element)
    return new
}

suspend fun Context.getBitmap(uri: Uri, size: Int): Bitmap? {
    val request = ImageRequest.Builder(this)
        .data(uri)
        .size(size)
        .precision(Precision.INEXACT)
        .allowHardware(true)
        .build()

    return when (val result = imageLoader.execute(request)) {
        is SuccessResult -> (result.drawable as BitmapDrawable).bitmap
        else -> null
    }
}

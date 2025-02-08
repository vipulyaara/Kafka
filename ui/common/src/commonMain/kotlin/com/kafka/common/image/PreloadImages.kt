package com.kafka.common.image

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

suspend fun preloadImages(context: PlatformContext, images: List<String>?) {
    withContext(Dispatchers.IO) {
        images?.forEach { image ->
            val request = ImageRequest.Builder(context)
                .data(image)
                .build()

            ImageLoader.Builder(context).build().enqueue(request)
        }
    }
}

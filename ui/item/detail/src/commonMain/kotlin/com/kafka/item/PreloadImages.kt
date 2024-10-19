package com.kafka.item

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.request.ImageRequest
import coil3.size.Size
import com.kafka.data.entities.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun preloadImages(context: PlatformContext, items: List<Item>?, size: Int = 200) {
    withContext(Dispatchers.IO) {
        items?.map { it.coverImage }?.forEach { image ->
            val request = ImageRequest.Builder(context)
                .data(image)
                .size(Size(size, size))
                .build()

            ImageLoader.Builder(context).build().enqueue(request)
        }
    }
}

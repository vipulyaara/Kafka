package com.kafka.item

import android.content.Context
import coil3.imageLoader
import coil3.request.ImageRequest
import coil3.size.Size
import com.kafka.data.entities.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun preloadImages(context: Context, items: List<Item>?, size: Int = 200) {
    withContext(Dispatchers.IO) {
        items?.map { it.coverImage }?.forEach { image ->
            val request = ImageRequest.Builder(context)
                .data(image)
                .size(Size(size, size))
                .build()
            context.imageLoader.enqueue(request)
        }
    }
}

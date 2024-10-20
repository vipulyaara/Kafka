package com.kafka.data.feature.item

import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.item.File
import java.net.URL
import javax.inject.Inject

class FileMapper @Inject constructor() {
    fun map(file: File, item: ItemDetail, prefix: String, localUri: String?) = file.run {
        val extension = name.split(".").last()
        com.kafka.data.entities.File(
            fileId = fileId,
            itemId = item.itemId,
            itemTitle = item.title,
            size = size?.firstOrNull()?.toLongOrNull(),
            name = name,
            title = title ?: name.removeSuffix(".$extension"),
            extension = extension,
            creators = creator ?: artist?.let { listOf(it) } ?: item.creators,
            duration = length?.let { mapDuration(it) },
            format = format.orEmpty(),
            url = URL("$prefix/$name").toString(),
            coverImage = item.coverImage,
            localUri = localUri,
            path = null,
            mediaType = item.mediaType
        )
    }

    private fun mapDuration(duration: String): Long {
        var durationInSeconds = 0L
        duration.split(":")
            .takeIf { it.size > 1 }
            ?.reversed()
            ?.forEachIndexed { index, time ->
                durationInSeconds += time.toInt() * (index * 60).coerceAtLeast(1)
            } ?: run {
            durationInSeconds = duration.toDouble().toLong()
        }

        return durationInSeconds
    }
}

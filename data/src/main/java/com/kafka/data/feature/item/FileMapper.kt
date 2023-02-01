package com.kafka.data.feature.item

import com.kafka.data.entities.isMp3
import com.kafka.data.entities.isPdf
import com.kafka.data.entities.isText
import com.kafka.data.model.item.File
import java.net.URL
import javax.inject.Inject

class FileMapper @Inject constructor() {
    fun map(
        file: File,
        itemId: String,
        itemTitle: String?,
        coverImage: String?,
        prefix: String,
        localUri: String?
    ) = file.run {
        val extension = name.split(".").last()
        com.kafka.data.entities.File(
            fileId = name,
            itemId = itemId,
            itemTitle = itemTitle,
            size = size?.toLongOrNull(),
            name = (title ?: name).dismissUpperCase(),
            extension = extension,
            creator = creator,
            time = length,
            format = format.orEmpty(),
            playbackUrl = if (format.isMp3()) URL("$prefix/$name").toString() else null,
            readerUrl = if (format.isPdf() || format.isText()) URL("$prefix/$name").toString() else null,
            downloadUrl = URL("$prefix/$name").toString(),
            coverImage = coverImage,
            localUri = localUri
        )
    }
}

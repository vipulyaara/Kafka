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
        com.kafka.data.entities.File(
            fileId = name,
            itemId = itemId,
            itemTitle = itemTitle,
            size = size?.toLongOrNull(),
            title = title?.dismissUpperCase() ?: "---",
            extension = name.split(".").last(),
            creator = creator,
            time = length,
            format = format,
            playbackUrl = if (format.isMp3()) URL("$prefix/$name").toString() else null,
            readerUrl = if (format.isPdf() || format.isText()) URL("$prefix/$name").toString() else null,
            coverImage = coverImage,
            localUri = localUri
        )
    }
}

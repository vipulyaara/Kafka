package com.kafka.data.feature.item

import com.kafka.data.dao.FileDao
import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType
import com.kafka.data.model.item.LibrivoxFileResponse
import javax.inject.Inject

class LibrivoxFileMapper @Inject constructor(private val fileDao: FileDao) {
    suspend fun map(response: LibrivoxFileResponse, item: ItemDetail) =
        response.sections.sortedBy { it.sectionNumber.toIntOrNull() }.mapIndexed { index, section ->
            val extension = section.listenUrl.split(".").last()

            File(
                fileId = section.id,
                itemId = item.itemId,
                itemTitle = item.title,
                size = 0,
                title = section.title,
                extension = extension,
                creators = item.creators,
                duration = section.playtime.toLongOrNull(),
                format = "readable/audio",
                url = section.listenUrl,
                coverImage = item.coverImage,
                localUri = fileDao.getOrNull(section.id)?.localUri,
                path = null,
                position = index,
                mediaType = MediaType.Audio
            )
        }
}

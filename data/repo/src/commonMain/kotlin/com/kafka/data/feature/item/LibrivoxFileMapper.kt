package com.kafka.data.feature.item

import com.kafka.data.entities.File
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.fileFormatAudio
import com.kafka.data.model.MediaType
import com.kafka.data.model.item.LibrivoxFileResponse
import me.tatarka.inject.annotations.Inject

@Inject
class LibrivoxFileMapper {
    fun map(response: LibrivoxFileResponse, item: ItemDetail) =
        response.sections.sortedBy { it.sectionNumber.toIntOrNull() }.mapIndexed { index, section ->
            val extension = section.listenUrl.split(".").last()

            File(
                fileId = section.id,
                itemId = item.itemId,
                itemTitle = item.title,
                size = 0,
                title = section.title,
                extension = extension,
                creators = item.creators.orEmpty(),
                duration = section.playtime.toLongOrNull(),
                format = fileFormatAudio,
                url = section.listenUrl,
                coverImage = item.coverImage,
                position = index,
                mediaType = MediaType.Audio
            )
        }
}

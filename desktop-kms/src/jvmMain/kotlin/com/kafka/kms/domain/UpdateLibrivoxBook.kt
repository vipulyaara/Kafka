package com.kafka.kms.domain

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.DownloadDao
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.model.MediaType
import com.kafka.kms.data.files.DirectoryPaths
import com.kafka.kms.data.files.DirectoryRepository
import com.kafka.kms.data.models.LibrivoxAudiobook
import com.kafka.kms.data.remote.LibrivoxService
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.io.File as JavaFile

@Inject
class UpdateLibrivoxBook(
    private val itemDao: ItemDao,
    private val fileDao: FileDao,
    private val downloadDao: DownloadDao,
    private val itemDetailDao: ItemDetailDao,
    private val librivoxService: LibrivoxService,
    private val directoryRepository: DirectoryRepository,
    private val dispatchers: CoroutineDispatchers
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) {
        return withContext(dispatchers.io) {
            debug { "UpdateLibrivoxBook fetch $params" }

            val audiobook = librivoxService.getAudiobookById(params)

            saveItem(audiobook)
            saveItemDetail(audiobook)
            saveFiles(audiobook)

            val itemDetail = itemDetailDao.get(audiobook.itemId)
            val repoId = itemDetail.repoId()

            directoryRepository.createAudioRepository(repoId)
            createOpfFile(audiobook, repoId)

            debug { "UpdateLibrivoxBook saved $audiobook" }
        }
    }

    private suspend fun saveItem(audiobook: LibrivoxAudiobook) {
        val item = Item(
            itemId = audiobook.itemId,
            title = audiobook.title,
            mediaType = MediaType.Audio,
            creators = listOf(audiobook.author),
            languages = listOf(audiobook.language),
            description = audiobook.description,
            coverImage = audiobook.coverImage,
            collections = listOf("librivox"),
            subjects = audiobook.subjects
        )

        itemDao.insert(item)
    }

    private suspend fun saveItemDetail(audiobook: LibrivoxAudiobook) {
        val itemDetail = ItemDetail(
            itemId = audiobook.itemId,
            description = audiobook.description,
            title = audiobook.title,
            mediaType = MediaType.Audio,
            creators = listOf(audiobook.author),
            translators = emptyList(),
            languages = listOf(audiobook.language),
            subjects = audiobook.subjects,
            collections = listOf("librivox"),
            publishers = listOf("LibriVox"),
            coverImages = listOfNotNull(audiobook.coverImage),
            rating = null
        )

        itemDetailDao.insert(itemDetail)
    }

    private suspend fun saveFiles(audiobook: LibrivoxAudiobook) {
        audiobook.sections.forEachIndexed { index, section ->
            val file = File(
                fileId = "${audiobook.itemId}_${index + 1}",
                itemId = audiobook.itemId,
                itemTitle = audiobook.title,
                title = section.title,
                mediaType = MediaType.Audio,
                extension = "mp3",
                format = "audio/mpeg",
                url = section.url,
                coverImage = audiobook.coverImage,
                creators = listOf(audiobook.author),
                position = index,
                duration = section.durationInSeconds.toLong()
            )

            fileDao.insert(file)
        }
    }

    private fun createOpfFile(audiobook: LibrivoxAudiobook, repoId: String) {
        val opfContent = buildString {
            append("""<?xml version="1.0" encoding="UTF-8"?>
                |<package xmlns="http://www.idpf.org/2007/opf" version="3.0">
                |  <metadata>
                |    <dc:title>${audiobook.title}</dc:title>
                |    <dc:creator>${audiobook.author}</dc:creator>
                |    <dc:language>${audiobook.language}</dc:language>
                |    <dc:identifier>librivox_${audiobook.itemId}</dc:identifier>
                |    <dc:description>${audiobook.description}</dc:description>
                |    <dc:publisher>LibriVox</dc:publisher>
                |  </metadata>
                |  <manifest>
            """.trimMargin())

            audiobook.sections.forEachIndexed { index, section ->
                append("""
                    |    <item id="section_${index + 1}" href="${section.url}" media-type="audio/mpeg" duration="${section.duration}"/>
                """.trimMargin())
            }

            append("""
                |  </manifest>
                |  <spine>
            """.trimMargin())

            audiobook.sections.forEachIndexed { index, _ ->
                append("""
                    |    <itemref idref="section_${index + 1}"/>
                """.trimMargin())
            }

            append("""
                |  </spine>
                |</package>
            """.trimMargin())
        }

        val audioDir = DirectoryPaths.audioPath(repoId)
        val opfFile = JavaFile(audioDir, "metadata.opf")
        opfFile.writeText(opfContent)
    }
}

fun librivoxItemId(id: String) = "librivox_$id" 
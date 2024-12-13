package com.kafka.kms.domain

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.FileDao
import com.kafka.data.dao.ItemDao
import com.kafka.data.dao.ItemDetailDao
import com.kafka.data.entities.File
import com.kafka.data.entities.Item
import com.kafka.data.entities.ItemDetail
import com.kafka.data.entities.fileFormatAudio
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
            rating = null,
            copyright = false,
            copyrightText = audiobook.copyrightText
        )

        itemDetailDao.insert(itemDetail)
    }

    private suspend fun saveFiles(audiobook: LibrivoxAudiobook) {
        // Group sections by their section_number and pick highest quality
        val highestQualitySections = audiobook.sections
            .groupBy { it.sectionNumber }
            .mapValues { (_, sections) -> 
                sections.maxByOrNull { section -> 
                    // Extract bitrate from URL - typically format is like "_64kb.mp3"
                    section.listenUrl.let { url ->
                        """_(\d+)kb\.mp3$""".toRegex().find(url)?.groupValues?.get(1)?.toIntOrNull() ?: 0
                    }
                }
            }
            .values
            .filterNotNull()
            .sortedBy { it.sectionNumber.toIntOrNull() ?: 0 }

        highestQualitySections.forEachIndexed { index, section ->
            val file = File(
                fileId = "${audiobook.itemId}_${section.id}",
                itemId = audiobook.itemId,
                itemTitle = audiobook.title,
                title = section.title,
                mediaType = MediaType.Audio,
                extension = "mp3",
                format = fileFormatAudio,
                url = section.listenUrl,
                coverImage = audiobook.coverImage,
                creators = section.readers.map { it.displayName },
                position = section.sectionNumber.toIntOrNull() ?: index,
                duration = section.playtime.split(":").let { parts ->
                    when (parts.size) {
                        3 -> parts[0].toLong() * 3600 + parts[1].toLong() * 60 + parts[2].toLong()
                        2 -> parts[0].toLong() * 60 + parts[1].toLong()
                        else -> 0L
                    }
                },
                languages = listOf(audiobook.language),
                size = null  // We don't have the size since we're not downloading
            )

            fileDao.insert(file)
        }
    }

    private suspend fun createOpfFile(audiobook: LibrivoxAudiobook, repoId: String) {
        val opfContent = buildString {
            append("""<?xml version="1.0" encoding="UTF-8"?>
                |<package xmlns="http://www.idpf.org/2007/opf" xmlns:dc="http://purl.org/dc/elements/1.1/" version="3.0">
                |  <metadata>
                |    <dc:title>${audiobook.title}</dc:title>
                |    <dc:creator>${audiobook.author}</dc:creator>
                |    <dc:language>${audiobook.language}</dc:language>
                |    <dc:identifier>librivox_${audiobook.itemId}</dc:identifier>
                |    <dc:description>${audiobook.description}</dc:description>
                |    <dc:publisher>LibriVox</dc:publisher>
                |    <dc:rights>${audiobook.copyrightText}</dc:rights>
                |    <dc:source>${audiobook.sourceUrl}</dc:source>
            """.trimMargin())

            // Add translators
            audiobook.translators.forEach { translator ->
                append("""
                    |    <dc:contributor opf:role="trl">${translator}</dc:contributor>
                """.trimMargin())
            }

            // Add subjects
            audiobook.subjects.forEach { subject ->
                append("""
                    |    <dc:subject>$subject</dc:subject>
                """.trimMargin())
            }

            // Add cover image if available
            audiobook.coverImage?.let { coverImage ->
                append("""
                    |    <meta name="cover" content="$coverImage"/>
                """.trimMargin())
            }

            append("""
                |  </metadata>
                |  <manifest>
            """.trimMargin())

            // Add cover image item if available
            audiobook.coverImage?.let { coverImage ->
                append("""
                    |    <item id="cover-image" href="$coverImage" media-type="image/jpeg"/>
                """.trimMargin())
            }

            audiobook.sections.forEachIndexed { index, section ->
                val sectionId = "section_${section.sectionNumber}"
                // Convert section number to zero-based position
                val position = section.sectionNumber.toIntOrNull()?.minus(1) ?: index
                append("""
                    |    <item id="$sectionId" href="${section.listenUrl}" media-type="audio/mpeg" duration="${section.playtime}">
                    |      <meta property="title">${section.title}</meta>
                    |      <meta property="duration">${section.playtime}</meta>
                    |      <meta property="position">$position</meta>
                    |      <meta property="readers">${section.readers.joinToString { it.displayName }}</meta>
                    |      <meta property="language">${section.language}</meta>
                    |    </item>
                """.trimMargin())
            }

            append("""
                |  </manifest>
                |  <spine>
            """.trimMargin())

            audiobook.sections.sortedBy { it.sectionNumber.toIntOrNull() ?: 0 }.forEach { section ->
                append("""
                    |    <itemref idref="section_${section.sectionNumber}"/>
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

        // Create a sections.json file with detailed section information
        val sectionsJson = buildString {
            append("[\n")
            audiobook.sections.forEachIndexed { index, section ->
                append("""
                    |  {
                    |    "id": "${section.id}",
                    |    "sectionNumber": "${section.sectionNumber}",
                    |    "title": "${section.title}",
                    |    "url": "${section.listenUrl}",
                    |    "duration": "${section.playtime}",
                    |    "language": "${section.language}",
                    |    "readers": ${section.readers.joinToString(prefix = "[", postfix = "]") { 
                            """{"id": "${it.readerId}", "name": "${it.displayName}"}"""
                        }}
                    |  }${if (index < audiobook.sections.size - 1) "," else ""}
                """.trimMargin())
            }
            append("\n]")
        }
        
        val sectionsFile = JavaFile(audioDir, "sections.json")
        sectionsFile.writeText(sectionsJson)
    }
}

fun librivoxItemId(id: String) = "librivox_$id" 
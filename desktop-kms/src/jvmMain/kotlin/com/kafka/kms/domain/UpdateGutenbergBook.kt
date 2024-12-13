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
import com.kafka.data.model.item.Publishers
import com.kafka.downloader.core.Downloader
import com.kafka.kms.data.files.DirectoryPaths.textPath
import com.kafka.kms.data.files.DirectoryRepository
import com.kafka.kms.data.models.CopyrightType.GutenbergCopyright
import com.kafka.kms.data.models.CopyrightType.GutenbergPublicDomain
import com.kafka.kms.data.models.GutenbergBook
import com.kafka.kms.data.remote.GutendexService
import com.kafka.kms.domain.gutenberg.CleanGutenbergHtml.cleanGutenbergHtml
import com.kafka.kms.domain.gutenberg.GutenbergChapters
import com.kafka.kms.domain.templates.Imprint
import com.kafka.kms.domain.templates.TitlePage
import com.kafka.kms.domain.templates.copyrightText
import com.kafka.kms.ui.directory.createDirectory
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateGutenbergBook(
    private val itemDao: ItemDao,
    private val fileDao: FileDao,
    private val downloadDao: DownloadDao,
    private val itemDetailDao: ItemDetailDao,
    private val gutendexService: GutendexService,
    private val directoryRepository: DirectoryRepository,
    private val downloader: Downloader,
    private val dispatchers: CoroutineDispatchers
) : Interactor<String, Unit>() {

    override suspend fun doWork(params: String) {
        return withContext(dispatchers.io) {
            debug { "UpdateGutenbergBook fetch $params" }

            val book = gutendexService.getBookById(params)

            saveItem(book)
            saveItemDetail(book)
            saveFiles(book)

            debug { "UpdateGutenbergBook saved $book" }

            val itemId = book.itemId
            val itemDetail = itemDetailDao.get(itemId)
            val file = fileDao.getByItemId(itemId).first { it.extension == "html" }
            val repoId = itemDetail.repoId()

            downloader.download(fileDao.getByItemId(book.itemId).first().fileId)
            val download = downloadDao.get(file.fileId)

            debug { "UpdateGutenbergBook downloaded ${file.url}" }

            directoryRepository.createRepository(repoId)

            cleanGutenbergHtml(
                sourceFilePath = download!!.filePath,
                repoId = repoId,
                htmlFileName = "pg${params.trim()}-images.html"
            )

            directoryRepository.cleanupDSStoreFiles()

            val copyright =
                if (book.copyright) GutenbergCopyright else GutenbergPublicDomain
            Imprint.addImprint(textPath(repoId).absolutePath, copyright)

            TitlePage.addTitlePage(textPath(repoId).absolutePath, itemDetail)

            val bodyPath = createDirectory(textPath(repoId), "body.xhtml")
            GutenbergChapters.splitFile(bodyPath.toPath(), textPath(repoId).toPath())

            debug { "UpdateGutenbergBook created" }
        }
    }

    private suspend fun saveItem(book: GutenbergBook) {
        val item = Item(
            itemId = book.itemId,
            title = book.title,
            mediaType = MediaType.Text,
            creators = book.authors.map { it.name },
            languages = book.languages,
            description = "",
            coverImage = "",
            collections = book.bookshelves,
            subjects = book.subjects
        )

        itemDao.insert(item)
    }

    private suspend fun saveItemDetail(book: GutenbergBook) {
        val itemDetail = ItemDetail(
            itemId = book.itemId,
            description = "",
            title = book.title,
            mediaType = MediaType.Text,
            creators = book.authors.map { it.name },
            translators = book.translators.map { it.name },
            languages = book.languages,
            subjects = book.subjects,
            collections = book.bookshelves,
            publishers = listOf(Publishers.gutenberg),
            coverImages = null,
            rating = null,
            copyright = book.copyright,
            copyrightText = (if (book.copyright) GutenbergCopyright else GutenbergPublicDomain)
                .copyrightText()
        )

        itemDetailDao.insert(itemDetail)
    }

    private suspend fun saveFiles(book: GutenbergBook) {
        val file = File(
            fileId = book.octetFileId,
            itemId = book.itemId,
            itemTitle = book.title,
            title = book.title + " - html",
            mediaType = MediaType.Text,
            extension = "html",
            format = "readable/html",
            url = book.formats.octetStream,
            coverImage = null,
            creators = book.authors.map { it.name },
            position = 0,
            duration = 0,
            languages = book.languages
        )

        fileDao.insert(file)
    }
}

fun ItemDetail.repoId() = run {
    val author = creators?.firstOrNull()?.lowercase()
        ?.replace(",", "")
        ?.replace(" ", "_") ?: "unknown"
    val bookTitle = title.lowercase()
        .replace(Regex("[^a-z0-9\\s]"), "")
        .replace("\\s+".toRegex(), "_")
    "${author}-${bookTitle}"
}

fun gutenbergItemId(id: String) = "gutenberg_$id"

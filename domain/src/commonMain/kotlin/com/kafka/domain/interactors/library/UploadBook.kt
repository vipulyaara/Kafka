package com.kafka.domain.interactors.library

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.domain.Interactor
import com.kafka.data.dao.FileDao
import com.kafka.data.entities.BookshelfDefaults
import com.kafka.data.entities.BookshelfItem
import com.kafka.data.entities.File
import com.kafka.data.feature.auth.AccountRepository
import com.kafka.data.feature.firestore.FirestoreGraph
import com.kafka.data.model.MediaType
import com.kafka.domain.interactors.UploadFileToFirebase
import com.kafka.domain.interactors.reader.ParseEbook
import io.github.vinceglb.filekit.core.FileKit
import io.github.vinceglb.filekit.core.PickerMode
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.extension
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okio.Buffer

@Inject
class UploadBook(
    private val parseEbook: ParseEbook,
    private val accountRepository: AccountRepository,
    private val firestoreGraph: FirestoreGraph,
    private val uploadFileToFirebase: UploadFileToFirebase,
    private val fileDao: FileDao,
    private val dispatchers: CoroutineDispatchers
) : Interactor<Unit, Unit>() {

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io) {
            val epubFile = FileKit.pickFile(
                type = PickerType.File(listOf("epub")),
                mode = PickerMode.Single,
                title = "Choose a readable file to upload",
                initialDirectory = "/downloads"
            )

            if (epubFile == null) {
                error("File was not selected")
            }

            val source = Buffer().write(epubFile.readBytes())
            val book = parseEbook(ParseEbook.Params(source = source)).getOrThrow()

            val uid = accountRepository.currentUserId
            val fileId = book.fileName

            val bookRefPath = "uploads/$uid/$fileId/$fileId.${epubFile.extension}"
            val coverRefPath = "uploads/$uid/$fileId/${book.coverImage?.absPath}"

            // Upload the book file
            val bookUrl = uploadFileToFirebase(
                UploadFileToFirebase.Params(byteArray = epubFile.readBytes(), refPath = bookRefPath)
            ).getOrThrow()

            // Upload the cover image if it exists
            val coverImageUrl = book.coverImage?.let { epubImage ->
                uploadFileToFirebase(
                    UploadFileToFirebase.Params(byteArray = epubImage.image, refPath = coverRefPath)
                ).getOrThrow()
            }.orEmpty()

            // Save the file to the database
            val fileEntity = File(
                fileId = fileId,
                itemId = fileId,
                itemTitle = book.title,
                title = book.fileName,
                url = bookUrl,
                mediaType = MediaType.Text,
                coverImage = coverImageUrl,
                creators = listOf(book.author),
                extension = epubFile.extension,
                format = "readable/${epubFile.extension}",
            )
            fileDao.insert(fileEntity)

            val document = firestoreGraph.listItemsCollection(
                uid = uid,
                listId = BookshelfDefaults.uploads.id
            )

            val bookshelfItem = BookshelfItem(
                itemId = book.fileName,
                itemTitle = book.title,
                creator = book.author,
                mediaType = MediaType.Text,
                coverImage = coverImageUrl
            )

            document
                .document(bookshelfItem.itemId)
                .set(bookshelfItem)
        }
    }
}

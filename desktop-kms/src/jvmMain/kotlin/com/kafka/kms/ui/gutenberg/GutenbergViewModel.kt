package com.kafka.kms.ui.gutenberg

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kafka.base.debug
import com.kafka.base.extensions.stateInDefault
import com.kafka.common.snackbar.SnackbarManager
import com.kafka.kms.data.models.GutenbergBook
import com.kafka.kms.domain.GetGutenbergBook
import com.kafka.kms.ui.directory.containerContent
import com.kafka.kms.ui.directory.createDirectory
import com.kafka.kms.ui.directory.createFile
import com.kafka.networking.localizedMessage
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject

@Inject
class GutenbergViewModel(
    private val getGutenbergBook: GetGutenbergBook,
    private val snackbarManager: SnackbarManager
) : ViewModel() {
    var bookId by mutableStateOf("")
    private var _book by mutableStateOf<GutenbergBook?>(null)

    var state = combine(getGutenbergBook.inProgress, snapshotFlow { _book }) { loading, book ->
        GutenbergState(gutenbergBook = book, loading = loading)
    }.stateInDefault(viewModelScope, GutenbergState())

    fun fetchBook() {
        viewModelScope.launch {
            try {
                val book = getGutenbergBook(bookId)
                _book = book.getOrNull()
            } catch (e: Exception) {
                snackbarManager.addMessage(e.localizedMessage())
            }
        }
    }

    fun createRepository(book: GutenbergBook) {
        val repo = book.run {
            val author = authors.firstOrNull()?.name?.lowercase()
                ?.replace(",", "")
                ?.replace(" ", "_") ?: "unknown"
            val bookTitle = title.lowercase()
                .replace(Regex("[^a-z0-9\\s]"), "")
                .replace("\\s+".toRegex(), "_")
            "${author}-${bookTitle}"
        }
        val baseDir = System.getProperty("user.home")
        val projectPath = listOf("StudioProjects", "kms-tools", "ebooks")

        // Create base ebooks directory
        val ebooksPath = createDirectory(baseDir, *projectPath.toTypedArray())
        val repoPath = createDirectory(ebooksPath, repo)

        val srcPath = createDirectory(repoPath, "src")

        // Create mimetype file in src directory
        createFile(srcPath, "mimetype").apply { writeText("application/epub+zip") }

        val epubPath = createDirectory(srcPath, "epub")
        val imagesPath = createDirectory(epubPath, "images")
        val cssPath = createDirectory(epubPath, "css")
        val textpath = createDirectory(epubPath, "text")

        val metaPath = createDirectory(repoPath, "META-INF")
        createFile(metaPath, "container.xml").apply { writeText(containerContent) }

        // Copy logo.svg to images directory
        val logoResource = javaClass.getResourceAsStream("/assets/logo.svg")
        logoResource?.use { input ->
            createFile(imagesPath, "logo.svg").outputStream().use { output ->
                input.copyTo(output)
            }
        }

        debug { "Creating directories $ebooksPath $repoPath $epubPath" }
    }

}

data class GutenbergState(val gutenbergBook: GutenbergBook? = null, val loading: Boolean = false)

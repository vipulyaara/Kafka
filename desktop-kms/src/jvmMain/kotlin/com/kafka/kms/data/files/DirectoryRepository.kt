package com.kafka.kms.data.files

import com.kafka.base.debug
import com.kafka.kms.ui.directory.containerContent
import com.kafka.kms.ui.directory.createDirectory
import com.kafka.kms.ui.directory.createFile
import me.tatarka.inject.annotations.Inject
import java.io.File

@Inject
class DirectoryRepository {

    fun createRepository(repoId: String) {
        // Create all directory structure
        val srcPath = DirectoryPaths.srcPath(repoId)
        val imagesPath = DirectoryPaths.imagesPath(repoId)
        val cssPath = DirectoryPaths.cssPath(repoId)
        val metaPath = DirectoryPaths.metaPath(repoId)

        // Create mimetype file in src directory
        createFile(srcPath, "mimetype").apply { writeText("application/epub+zip") }

        // Create container.xml in META-INF
        createFile(metaPath, "container.xml").apply { writeText(containerContent) }

        // Copy logo.svg to images directory
        val logoResource = javaClass.getResourceAsStream("/assets/logo.svg")
        logoResource?.use { input ->
            createFile(imagesPath, "logo.svg").outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // Copy CSS files to css directory
        listOf("core.css", "kafka.css", "local.css").forEach { cssFile ->
            javaClass.getResourceAsStream("/css/$cssFile")?.use { input ->
                createFile(cssPath, cssFile).outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        debug { "Creating directories " }
    }

    fun createAudioRepository(repoId: String) {
        // Create audio directory structure
        val audioPath = DirectoryPaths.audioPath(repoId)
        val metaPath = DirectoryPaths.audioMetaPath(repoId)

        // Create container.xml in META-INF
        createFile(metaPath, "container.xml").apply { writeText(containerContent) }

        debug { "Creating audio directories for $repoId" }
    }

    /**
     * Removes all .DS_Store files from the ebooks directory and its subdirectories
     * @return The number of .DS_Store files removed
     */
    fun cleanupDSStoreFiles(): Int {
        var count = 0
        if (DirectoryPaths.ebooksPath.exists() && DirectoryPaths.ebooksPath.isDirectory) {
            count += removeDSStoreFiles(DirectoryPaths.ebooksPath)
        }
        if (DirectoryPaths.audiobooksPath.exists() && DirectoryPaths.audiobooksPath.isDirectory) {
            count += removeDSStoreFiles(DirectoryPaths.audiobooksPath)
        }
        return count
    }

    /**
     * Helper function that recursively removes .DS_Store files
     * @param directory The root directory to start the removal process
     * @return The number of .DS_Store files removed
     */
    private fun removeDSStoreFiles(directory: File): Int {
        var removedCount = 0

        directory.walkTopDown().forEach { file ->
            if (file.name == ".DS_Store") {
                val deleted = file.delete()
                if (deleted) {
                    removedCount++
                    debug { "Removed .DS_Store file at: ${file.absolutePath}" }
                } else {
                    debug { "Failed to remove .DS_Store file at: ${file.absolutePath}" }
                }
            }
        }

        return removedCount
    }
}

object DirectoryPaths {
    private val baseDir = System.getProperty("user.home")
    private val projectPath = listOf("StudioProjects", "kms-tools")

    // Ebooks paths
    val ebooksPath = createDirectory(baseDir, *projectPath.toTypedArray(), "ebooks")

    fun repoPath(repoId: String) = createDirectory(ebooksPath, repoId)
    fun srcPath(repoId: String) = createDirectory(repoPath(repoId), "src")
    fun epubPath(repoId: String) = createDirectory(srcPath(repoId), "epub")
    fun imagesPath(repoId: String) = createDirectory(epubPath(repoId), "images")
    fun cssPath(repoId: String) = createDirectory(epubPath(repoId), "css")
    fun textPath(repoId: String) = createDirectory(epubPath(repoId), "text")
    fun metaPath(repoId: String) = createDirectory(repoPath(repoId), "META-INF")

    // Audiobooks paths
    val audiobooksPath = createDirectory(baseDir, *projectPath.toTypedArray(), "audiobooks")

    fun audioRepoPath(repoId: String) = createDirectory(audiobooksPath, repoId)
    fun audioPath(repoId: String) = createDirectory(audioRepoPath(repoId), "audio")
    fun audioMetaPath(repoId: String) = createDirectory(audioRepoPath(repoId), "META-INF")
}


/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.datmusic.downloader

import android.content.Context
import android.net.Uri
import androidx.core.net.toFile
import androidx.documentfile.provider.DocumentFile
import com.kafka.data.entities.File
import java.io.FileNotFoundException

val filenameIllegalChars = setOf('|', '/', '\\', '?', '*', '<', '>', '"', ':')
private fun String.cleanIllegalChars(chars: Set<Char> = filenameIllegalChars, replacement: Char = '_') =
    map { if (it in chars) replacement else it }.joinToString("")

fun Uri.toDocumentFile(context: Context) = when (scheme) {
    "file" -> DocumentFile.fromFile(toFile())
    else -> DocumentFile.fromTreeUri(context, this)
} ?: error("Couldn't resolve uri to document file")

fun DocumentFile.getOrCreateDir(name: String) = findFile(name.cleanIllegalChars())
    ?: createDirectory(name.cleanIllegalChars())
    ?: error("Couldn't create folder:$name")

fun File.createDocumentFile(parent: DocumentFile): DocumentFile {
    var newFile = parent.createFile(format, title.cleanIllegalChars())
    // normal saf would return new file name if file already existed,
    // so we need to have similar behavior for raw files (document files opened via DocumentFile.fromFile)
    if (newFile == null && parent.uri.scheme == "file") {
        newFile = parent.listFiles().find { it.name?.startsWith(title) == true }
    }
    return newFile ?: error("Couldn't create document file")
}

fun File.documentFile(parent: DocumentFile): DocumentFile {
    if (!parent.exists()) {
        throw FileNotFoundException("Parent folder doesn't exist")
    }

    val mainArtist = creator ?: "Unknown"
    val artistFolder = parent.getOrCreateDir(mainArtist)
    val albumName = itemTitle ?: ""

    return when (albumName.isBlank()) {
        true -> createDocumentFile(artistFolder)
        else -> {
            val albumFolder = artistFolder.getOrCreateDir(albumName)
            createDocumentFile(albumFolder)
        }
    }
}

/**
 * Copyright (c) [2022 - Present] Stɑrry Shivɑm
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kafka.reader.epub.cache

import com.kafka.base.ApplicationInfo
import com.kafka.base.debug
import com.kafka.reader.epub.models.EpubBook
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.protobuf.ProtoBuf
import kotlinx.serialization.protobuf.schema.ProtoBufSchemaGenerator
import me.tatarka.inject.annotations.Inject
import java.io.File

/**
 * A cache storage based on Protocol Buffers for storing [EpubBook] objects.
 * The cache is stored in the app's cache directory.
 *
 * @param context The context of the application.
 */
@Inject
class EpubCache(private val applicationInfo: ApplicationInfo) {

    companion object {
        private const val TAG = "EpubCache"
        private const val EPUB_CACHE = "epub_cache"
        private const val CACHE_VERSION_FILE = "cache_version"

        // Increment this if the cache format changes.
        private const val EPUB_CACHE_VERSION = 2
    }

    init {
        create()
        checkCacheVersion()
    }

    @OptIn(ExperimentalSerializationApi::class)
    private val protobuf = ProtoBuf { encodeDefaults = true }

    private fun getPath(): String {
        return applicationInfo.cachePath() + File.separator + EPUB_CACHE
    }

    private fun getVersionFilePath(): String {
        return getPath() + File.separator + CACHE_VERSION_FILE
    }

    // Checks if the cache version matches the current version.
    private fun checkCacheVersion() {
        debug { "Checking cache version" }
        val versionFile = File(getVersionFilePath())
        if (versionFile.exists()) {
            val cachedVersion = versionFile.readText().toIntOrNull()
            if (cachedVersion != EPUB_CACHE_VERSION) {
                debug { "Cache version mismatch, clearing cache" }
                clear()
                create()
                saveCacheVersion()
            }
        } else {
            debug { "Cache version not found, saving cache version" }
            saveCacheVersion()
        }
    }

    // Saves the current cache version.
    private fun saveCacheVersion() {
        debug { "Saving cache version" }
        val versionFile = File(getVersionFilePath())
        versionFile.writeText(EPUB_CACHE_VERSION.toString())
    }

    private fun create() {
        val cacheDir = File(getPath())
        if (!cacheDir.exists()) {
            debug { "Creating cache directory" }
            cacheDir.mkdirs()
        }
    }

    private fun clear() {
        val cacheDir = File(getPath())
        if (cacheDir.exists()) {
            debug { "Clearing cache directory" }
            cacheDir.deleteRecursively()
        }
    }

    // Used for debugging purposes.
    @Suppress("unused")
    @OptIn(ExperimentalSerializationApi::class)
    private fun printSchema() {
        val protoSchema =
            ProtoBufSchemaGenerator.generateSchemaText(EpubBook.serializer().descriptor)
        debug { "Proto schema: $protoSchema" }
    }

    /**
     * Inserts a book into the cache.
     *
     * @param book The book to insert.
     * @param filepath The path to the book file.
     */
    @OptIn(ExperimentalSerializationApi::class)
    fun put(book: EpubBook, filepath: String) {
        debug { "Inserting book into cache: ${book.title}" }
        val fileName = File(filepath).nameWithoutExtension
        val bookFile = File(getPath(), "$fileName.protobuf")
        val protoBytes = protobuf.encodeToByteArray(EpubBook.serializer(), book)
        bookFile.writeBytes(protoBytes)
    }

    /**
     * Gets a book from the cache.
     *
     * @param filepath The path to the book file.
     * @return The book if it is cached, null otherwise.
     */
    @OptIn(ExperimentalSerializationApi::class)
    fun get(filepath: String): EpubBook? {
        debug { "Getting book from cache: $filepath" }
        val fileName = File(filepath).nameWithoutExtension
        val bookFile = File(getPath(), "$fileName.protobuf")
        return if (bookFile.exists()) {
            debug { "Book found in cache: $filepath" }
            val protoBytes = bookFile.readBytes()
            protobuf.decodeFromByteArray(EpubBook.serializer(), protoBytes)
        } else {
            debug { "Book not found in cache: $filepath" }
            null
        }
    }

    /**
     * Removes a book from the cache.
     *
     * @param filepath The path to the book file.
     */
    fun remove(filepath: String): Boolean {
        debug { "Removing book from cache: $filepath" }
        val fileName = File(filepath).nameWithoutExtension
        val bookFile = File(getPath(), "$fileName.protobuf")
        return if (bookFile.exists()) {
            bookFile.delete()
        } else {
            false
        }
    }

    /**
     * Checks if a book is cached.
     *
     * @param filepath The path to the book file.
     * @return True if the book is cached, false otherwise.
     */
    fun isCached(filepath: String): Boolean {
        val fileName = File(filepath).nameWithoutExtension
        val bookFile = File(getPath(), "$fileName.protobuf")
        return bookFile.exists()
    }
}

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
package kafka.reader.core.parser

import com.fleeksoft.ksoup.nodes.Element
import com.fleeksoft.ksoup.nodes.Node
import com.kafka.base.ApplicationInfo
import com.kafka.base.ApplicationScope
import com.kafka.base.debug
import com.kafka.base.errorLog
import kafka.reader.core.cache.EpubCache
import kafka.reader.core.models.ContentElement
import kafka.reader.core.models.EpubBook
import kafka.reader.core.models.EpubChapter
import kafka.reader.core.models.EpubImage
import kafka.reader.core.models.NavPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import okio.BufferedSource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.Source
import okio.buffer
import okio.use

/**
 * Parses an EPUB file and creates an [EpubBook] object.
 */
@Inject
@ApplicationScope
class EpubParser(
    private val epubCache: EpubCache,
    private val applicationInfo: ApplicationInfo,
    private val fileSystem: FileSystem
) {

    /**
     * Represents an EPUB document.
     *
     * @param metadata The metadata of the document.
     * @param manifest The manifest of the document.
     * @param spine The spine of the document.
     * @param opfFilePath The file path of the OPF file.
     */
    data class EpubDocument(
        val metadata: Node, val manifest: Node, val spine: Node, val opfFilePath: String
    )

    /**
     * Represents an item in the EPUB manifest.
     *
     * @param id The ID of the item.
     * @param absPath The absolute path of the item.
     * @param mediaType The media type of the item.
     * @param properties The properties of the item.
     */
    data class EpubManifestItem(
        val id: String, val absPath: String, val mediaType: String, val properties: String
    )

    /**
     * Represents an EPUB file.
     *
     * @param absPath The absolute path of the file.
     * @param data The file data.
     */
    data class EpubFile(val absPath: String, val data: ByteArray) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as EpubFile

            if (absPath != other.absPath) return false
            return data.contentEquals(other.data)
        }

        override fun hashCode(): Int {
            var result = absPath.hashCode()
            result = 31 * result + data.contentHashCode()
            return result
        }
    }

    /**
     * Creates an [EpubBook] object from an EPUB file.
     *
     * @param filePath The file path of the EPUB file.
     * @return The [EpubBook] object.
     */
    suspend fun createEpubBook(filePath: String): EpubBook {
        return withContext(Dispatchers.IO) {
//            val epubBook = epubCache.get(filePath)
//            if (epubBook != null) {
//                debug { "EpubBook found in cache" }
//                return@withContext epubBook
//            }
            debug { "Parsing EPUB file: $filePath" }
            val files = getZipFilesFromFile(filePath)
            val document = createEpubDocument(files)
            val book = parseAndCreateEbook(files, document)
            epubCache.put(book, filePath)
            return@withContext book
        }
    }

    /**
     * Creates an [EpubBook] object from an EPUB input stream.
     *
     * Note: Caller is responsible for closing the source.
     *
     * @param source The source of the EPUB file.
     * @return The [EpubBook] object.
     */
    suspend fun createEpubBook(source: Source): EpubBook {
        return withContext(Dispatchers.IO) {
            // todo: add cache

            val (files, document) = getZipFilesAndDocument(source)
            val book = parseAndCreateEbook(files, document)

            return@withContext book
        }
    }

    /**
     * Checks if an EPUB book is cached.
     *
     * @param filePath The file path of the EPUB file.
     * @return True if the book is cached, false otherwise.
     */
    fun isBookCached(filePath: String): Boolean {
        return epubCache.isCached(filePath)
    }

    /**
     * Removes an EPUB book from the cache.
     *
     * @param filePath The file path of the EPUB file.
     */
    fun removeBookFromCache(filePath: String): Boolean {
        return epubCache.remove(filePath)
    }

    /**
     * Parses and creates an [EpubBook] object from the EPUB files and document.
     * This function is called from [createEpubBook] and [createEpubBook].
     *
     * @param files The EPUB files.
     * @param document The EPUB document.
     * @return The [EpubBook] object.
     */
    private suspend fun parseAndCreateEbook(
        files: Map<String, EpubFile>, document: EpubDocument): EpubBook = withContext(Dispatchers.IO) {
        val metadataTitle =
            document.metadata.selectFirstChildTag("dc:title")?.text() ?: "Unknown Title"
        val metadataAuthor =
            document.metadata.selectFirstChildTag("dc:creator")?.text() ?: "Unknown Author"
        val metadataLanguage = document.metadata.selectFirstChildTag("dc:language")?.text() ?: "en"

        val hrefRootPath = document.opfFilePath.toPath().parent ?: "".toPath()
        val manifestItems = getManifestItems(manifest = document.manifest, hrefRootPath)

        // Find and load CSS files
        val cssFiles = findCssFiles(document.manifest as Element, hrefRootPath)
        val cssContent = loadCssContent(cssFiles, files)

        // Find the table of contents (toc.ncx) file
        val tocFileItem = manifestItems.values.firstOrNull {
            it.absPath.endsWith(".ncx", ignoreCase = true)
        }

        val tocXhtmlFileItem = manifestItems.values.firstOrNull {
            it.absPath.endsWith("/toc.xhtml", ignoreCase = true)
        }

        // Find the nested navPoints in the table of contents
        val tocNavPoints = when {
            tocFileItem != null -> {
                val tocFile = files[tocFileItem.absPath]
                val tocDocument = tocFile?.let { parseXMLFile(it.data) }
                findNestedNavPoints(tocDocument?.selectFirstTag("navMap"))
            }
            tocXhtmlFileItem != null -> {
                val tocFile = files[tocXhtmlFileItem.absPath]
                val tocDocument = tocFile?.let { parseXMLFile(it.data) }
                val navElement = tocDocument?.selectFirst("nav#toc")
                    ?: tocDocument?.selectFirst("nav[epub:type=toc]")
                findNestedNavPointsFromXhtml(navElement?.selectFirst("ol"))
            }
            else -> emptyList()
        }

        // Determine parsing method based on ToC presence and validity
        val chapters = parseUsingSpine(document.spine, manifestItems, files, cssContent)

        debug { "Parsing images" }
        val images = parseImages(manifestItems, files)

        val metadataCoverId = getMetadataCoverId(document.metadata)
        val coverImage = parseCoverImage(metadataCoverId, manifestItems, files)

        debug { "EpubBook created" }
        return@withContext EpubBook(
            fileName = metadataTitle.asFileName(),
            title = metadataTitle,
            author = metadataAuthor,
            language = metadataLanguage,
            chapters = chapters,
            navPoints = tocNavPoints,
            coverImage = coverImage,
            images = images
        )
    }

    /**
     * Get all of the files located in the EPUB archive and the EPUB document (content.opf).
     *
     * @param source The source of the EPUB file.
     * @return A pair of the EPUB files and the EPUB document.
     */
    private suspend fun getZipFilesAndDocument(
        source: Source
    ): Pair<Map<String, EpubFile>, EpubDocument> {
        var files = getZipFilesFromStream(source)
        val epubDocument = try {
            createEpubDocument(files)
        } catch (exc: EpubParserException) {
            // In some rare cases, the ZipInputStream does not contain or fails to read all of the files
            // required to parse the EPUB archive, even though the zip file itself contains them.
            // In such cases, retry parsing the EPUB file by directly using the ZipFile API.
            // Since ZipFile requires a file path, we need to create a temporary file from the input stream.
            //
            // The reasons for this issue are unknown and may be related to how the EPUB file is compressed,
            // i.e., whether it is missing some metadata or file/folder entry in its header, or how the
            // ZipInputStream reads the file.
            //
            // If anyone knows the exact reason for this issue or has dealt with it before, please
            // let me know, or feel free to create a PR with a better solution.
            if (exc.message == "META-INF/container.xml file missing"
                || exc.message == ".opf file missing"
            ) {
                errorLog {
                    "Failed to parse EPUB file using ZipInputStream due to missing files required for parsing! Retrying using ZipFile by creating temporary file from input stream."
                }
                debug { "Resetting input stream position to beginning" }
                withContext(Dispatchers.IO) {
                    when (source) {
                        is BufferedSource -> source.buffer.clear()
                        else -> source.buffer().buffer.clear()
                    }
                }
                files = getZipFilesFromTempFile(source)
                createEpubDocument(files)
            } else {
                throw exc
            }
        }

        return Pair(files, epubDocument)
    }

    /**
     * Get all of the files located in the EPUB archive.
     * This method is called from [createEpubBook].
     *
     * @param filePath The file path of the EPUB file.
     * @return The EPUB files.
     */
    private fun getZipFilesFromFile(filePath: String): Map<String, EpubFile> {
        return fileSystem.source(filePath.toPath()).use { source ->
            ZipReader(source).use { zip ->
                zip.entries()
                    .filterNot { it.isDirectory }
                    .map { entry ->
                        EpubFile(absPath = entry.name, data = entry.data)
                    }
                    .associateBy { it.absPath }
            }
        }
    }

    /**
     * Copy the source to a temporary file and get all of the files located in the EPUB archive.
     * This is a fallback method if the ZipInputStream fails to read all of the files required for parsing.
     * This method is called from [getZipFilesAndDocument].
     *
     * @param source The source of the EPUB file.
     * @return The EPUB files.
     */
    private fun getZipFilesFromTempFile(source: Source): Map<String, EpubFile> {
        val tempPath = applicationInfo.cachePath().toPath() / "_zip_temp.epub"

        // Write to temp file using okio
        fileSystem.sink(tempPath).buffer().use { sink ->
            sink.writeAll(source)
        }

        val epubFile = getZipFilesFromFile(tempPath.toString())
        fileSystem.delete(tempPath)
        return epubFile
    }

    /**
     * Get all of the files located in the EPUB archive from the input stream.
     * This method is called from [getZipFilesAndDocument] and [getZipFilesFromTempFile].
     *
     * @param source The source of the EPUB file.
     * @return The EPUB files.
     */
    private suspend fun getZipFilesFromStream(
        source: Source
    ): Map<String, EpubFile> = withContext(Dispatchers.IO) {
        source.buffer().use { bufferedSource ->
            ZipReader(bufferedSource).use { zip ->
                zip.entries()
                    .filterNot { it.isDirectory }
                    .map { entry ->
                        EpubFile(absPath = entry.name, data = entry.data)
                    }
                    .associateBy { it.absPath }
            }
        }
    }

    /**
     * Create an [EpubDocument] object from the EPUB files.
     * This method is called from [createEpubBook] and [getZipFilesAndDocument].
     *
     * @param files The EPUB files.
     * @return The [EpubDocument] object.
     */
    @Throws(EpubParserException::class)
    private fun createEpubDocument(files: Map<String, EpubFile>): EpubDocument {
        val container = files["META-INF/container.xml"]
            ?: throw EpubParserException("META-INF/container.xml file missing")

        val opfFilePath = parseXMLFile(container.data).selectFirstTag("rootfile")
            ?.getAttributeValue("full-path")?.decodeURL()
            ?: throw EpubParserException("Invalid container.xml file")

        val opfFile = files[opfFilePath] ?: throw EpubParserException(".opf file missing")

        val document = parseXMLFile(opfFile.data)
        val metadata = document.selectFirstTag("metadata")
            ?: document.selectFirstTag("opf:metadata")
            ?: throw EpubParserException(".opf file metadata section missing")
        val manifest = document.selectFirstTag("manifest")
            ?: document.selectFirstTag("opf:manifest")
            ?: throw EpubParserException(".opf file manifest section missing")
        val spine = document.selectFirstTag("spine")
            ?: document.selectFirstTag("opf:spine")
            ?: throw EpubParserException(".opf file spine section missing")

        return EpubDocument(metadata, manifest, spine, opfFilePath)
    }

    /**
     * Get the cover ID from the EPUB metadata.
     * This method is called from [parseAndCreateEbook].
     *
     * @param metadata The metadata of the EPUB document.
     * @return The cover ID.
     */
    private fun getMetadataCoverId(metadata: Node): String? {
        return metadata.selectChildTag("meta")
            .ifEmpty { metadata.selectChildTag("opf:meta") }
            .find { it.getAttributeValue("name") == "cover" }?.getAttributeValue("content")
    }

    /**
     * Get the manifest items from the EPUB manifest.
     * This method is called from [parseAndCreateEbook].
     *
     * @param manifest The manifest of the EPUB document.
     * @param hrefRootPath The root path of the href attribute.
     * @return The manifest items.
     */
    private fun getManifestItems(
        manifest: Node,
        hrefRootPath: Path
    ): Map<String, EpubManifestItem> {
        return manifest.selectChildTag("item")
            .ifEmpty { manifest.selectChildTag("opf:item") }
            .map {
                EpubManifestItem(
                    id = it.attr("id"),
                    absPath = it.attr("href").decodeURL().getCanonicalPath(hrefRootPath.toString()),
                    mediaType = it.attr("media-type"),
                    properties = it.attr("properties")
                )
            }.associateBy { it.id }
    }

    /**
     * Find all the nested navPoints in the table of contents (ToC) file.
     * This method is called from [parseAndCreateEbook].
     *
     * @param element The element to search for nested navPoints.
     * @return The list of nested navPoints.
     */
    private fun findNestedNavPoints(navMap: Element?): List<NavPoint> {
        val navPoints = mutableListOf<NavPoint>()

        navMap?.childElements?.filter { it.tagName() == "navPoint" }?.forEach { navPointElement ->
            val navPoint = NavPoint(
                title = navPointElement.selectFirstChildTag("navLabel")?.selectFirstChildTag("text")
                    ?.text().orEmpty(),
                src = navPointElement.selectFirstChildTag("content")?.getAttributeValue("src")
                    .orEmpty(),
                children = findNestedNavPoints(navPointElement)
            )
            navPoints.add(navPoint)
        }

        return navPoints
    }

    /**
     * Find all the nested navPoints in the XHTML table of contents (ToC) file.
     * This method parses the XHTML TOC format where <li> elements represent NavPoints
     * and nested <ol> tags define the structure.
     *
     * @param element The root element containing the TOC structure.
     * @return The list of nested navPoints.
     */
    private fun findNestedNavPointsFromXhtml(element: Element?): List<NavPoint> {
        val navPoints = mutableListOf<NavPoint>()

        element?.selectChildTag("li")?.forEach { liElement ->
            val anchor = liElement.selectFirst("a")
            if (anchor != null) {
                val navPoint = NavPoint(
                    title = anchor.text().trim(),
                    src = anchor.getAttributeValue("href").orEmpty(),
                    children = liElement.selectFirst("ol")?.let { findNestedNavPointsFromXhtml(it) } ?: emptyList()
                )
                navPoints.add(navPoint)
            }
        }

        return navPoints
    }

    /**
     * Generate a stable ID for a chapter based on multiple unique identifiers.
     * This ensures the same chapter always gets the same ID across different parsing sessions.
     *
     * @param absPath The absolute path of the chapter
     * @param title The chapter titlel
     * @param contentElements The chapter's content elements
     * @param navPoint Optional navPoint element containing additional metadata
     * @return The generated stable ID
     */
    private fun generateStableChapterId(
        absPath: String,
        title: String,
        contentElements: List<ContentElement>,
        navPoint: Element? = null
    ): String {
        val uniqueIdentifiers = buildList {
            // Base path and title
            add(absPath)
            add(title)

            // NavPoint specific identifiers
            navPoint?.let { nav ->
                add("nav-${nav.id()}")
                add("order-${nav.attr("playOrder")}")
                nav.selectFirstChildTag("content")?.attr("src")?.let { add("src-$it") }
            }

            // Content based identifiers
            if (contentElements.isNotEmpty()) {
                // First paragraph or heading text (truncated)
                contentElements.firstOrNull { it is ContentElement.Text || it is ContentElement.Heading }
                    ?.let { element ->
                        when (element) {
                            is ContentElement.Text -> element.content.take(50)
                            is ContentElement.Heading -> element.content.take(50)
                            else -> null
                        }?.let { add("content-$it") }
                    }

                // Content structure fingerprint
                add(
                    "structure-" + contentElements.take(5)
                        .joinToString("-") { it::class.simpleName ?: "" })
            }
        }

        val uniqueString = uniqueIdentifiers.joinToString("#")
        return uniqueString.hashCode().toString(16)
    }

    /**
     * Parse the EPUB file using the spine.
     * This method is called from [parseAndCreateEbook].
     *
     * @param spine The spine of the EPUB document.
     * @param manifestItems The manifest items.
     * @param files The EPUB files.
     * @return The list of parsed chapters.
     */
    private fun parseUsingSpine(
        spine: Node,
        manifestItems: Map<String, EpubManifestItem>,
        files: Map<String, EpubFile>,
        cssContent: String,
    ): List<EpubChapter> {
        var chapterIndex = 0
        val chapterExtensions = listOf("xhtml", "xml", "html", "htm").map { ".$it" }

        // Try to find TOC landmarks for level information
        val landmarkLevels = try {
            val tocFile = files.entries.find { it.key.endsWith("toc.xhtml", ignoreCase = true) }
            val landmarks =
                tocFile?.let { parseXMLFile(it.value.data).selectFirst("nav#landmarks") }
            landmarks?.selectChildTag("ol")
                ?.firstOrNull()
                ?.selectChildTag("li")
                ?.mapNotNull { li ->
                    li.selectFirst("a")?.attr("href")?.let { href ->
                        href.substringAfterLast("/") to 0
                    }
                }?.toMap() ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }

        val stylesheetParser = StylesheetParser(cssContent)

        return spine.selectChildTag("itemref")
            .ifEmpty { spine.selectChildTag("opf:itemref") }
            .mapNotNull { manifestItems[it.attr("idref")] }
            .filter { item ->
                chapterExtensions.any {
                    item.absPath.endsWith(it, ignoreCase = true)
                } || item.mediaType.startsWith("image/")
            }
            .mapNotNull { item ->
                files[item.absPath]?.let { file ->
                    val parser = EpubXMLFileParser(
                        fileAbsolutePath = file.absPath,
                        data = file.data,
                        zipFile = files,
                        stylesheetParser = stylesheetParser
                    )

                    val contentElements = parser.parseAsDocument()
                    if (contentElements.isNotEmpty()) {
                        // Extract title from h2 with epub:type="title" if present
                        val titleElement = parser.findTitleElement()
                        val title = titleElement?.text()?.takeIf { it.isNotBlank() }
                            ?: contentElements.firstOrNull { it is ContentElement.Heading }
                                ?.let { (it as ContentElement.Heading).content }

                        if (title != null) chapterIndex++

                        val fileName = file.absPath.substringAfterLast("/")
                        val level = landmarkLevels[fileName] ?: 0

                        EpubChapter(
                            chapterId = generateStableChapterId(
                                absPath = file.absPath,
                                title = title?.takeIf { it.isNotBlank() }
                                    ?: "Chapter $chapterIndex",
                                contentElements = contentElements
                            ),
                            absPath = file.absPath,
                            title = title?.takeIf { it.isNotBlank() } ?: "Chapter $chapterIndex",
                            level = level,
                            contentElements = contentElements
                        ).also { debug { "Added Chapter: $it" } }
                    } else {
                        debug { "Empty chapter: ${file.absPath}" }
                        null
                    }
                }
            }
            .filter { it.contentElements.isNotEmpty() }
            .toList()
    }

    /**
     * Parse the EPUB images.
     * This method is called from [parseAndCreateEbook].
     *
     * @param manifestItems The manifest items.
     * @param files The EPUB files.
     * @return The list of parsed images.
     */
    private fun parseImages(
        manifestItems: Map<String, EpubManifestItem>,
        files: Map<String, EpubFile>
    ): List<EpubImage> {
        val imageExtensions =
            listOf("png", "gif", "raw", "jpg", "jpeg", "webp", "svg").map { ".$it" }

        val unlistedImages = files.asSequence()
            .filter { (_, file) ->
                imageExtensions.any { file.absPath.endsWith(it, ignoreCase = true) }
            }
            .map { (_, file) ->
                EpubImage(absPath = file.absPath, image = file.data)
            }

        val listedImages = manifestItems.asSequence()
            .map { it.value }
            .filter { it.mediaType.startsWith("image") }
            .mapNotNull { files[it.absPath] }
            .map { EpubImage(absPath = it.absPath, image = it.data) }

        return (listedImages + unlistedImages).distinctBy { it.absPath }.toList()
    }

    inline fun <T> ZipReader.use(block: (ZipReader) -> T): T {
        try {
            return block(this)
        } finally {
            close()
        }
    }

    private fun parseCoverImage(
        metadataCoverId: String?,
        manifestItems: Map<String, EpubManifestItem>,
        files: Map<String, EpubFile>
    ): EpubImage? {
        return manifestItems[metadataCoverId]?.let { manifestItem ->
            files[manifestItem.absPath]?.let { file ->
                EpubImage(absPath = file.absPath, image = file.data)
            }
        }?.takeIf { it.image.isNotEmpty() }
    }

    // Add this extension function to EpubXMLFileParser
    private fun EpubXMLFileParser.findTitleElement(): Element? {
        // Skip title search for titlepage.xhtml
        if (fileAbsolutePath.endsWith("titlepage.xhtml", ignoreCase = true)) {
            return null
        }

        return document.selectFirst("h2[epub:type=title]")
            ?: document.selectFirst("h1[epub:type=title]")
            ?: document.selectFirst("h3[epub:type=title]")
            ?: document.selectFirst("h4[epub:type=title]")
    }

    private fun findCssFiles(manifest: Element, hrefRootPath: Path): List<String> {
        return manifest.select("item")
            .filter { it.attr("media-type") == "text/css" }
            .map { it.attr("href").decodeURL().getCanonicalPath(hrefRootPath.toString()) }
            .filter { it.endsWith(".css", ignoreCase = true) }
    }

    private fun loadCssContent(cssFiles: List<String>, files: Map<String, EpubFile>): String {
        debug { "Loading CSS content: ${cssFiles.joinToString(", ")}" }
        return cssFiles.joinToString("\n") { cssPath ->
            try {
                files[cssPath]?.let { it.data.decodeToString() } ?: ""
            } catch (e: Exception) {
                println("Failed to read CSS file: $cssPath")
                ""
            }
        }
    }
}

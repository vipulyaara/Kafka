package com.kafka.kms.domain

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Document
import com.fleeksoft.ksoup.parser.Parser
import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.kms.data.files.DirectoryPaths
import com.kafka.kms.data.files.DirectoryPaths.textPath
import com.kafka.kms.data.files.DirectoryRepository
import com.kafka.kms.domain.gutenberg.GutenbergChapters
import com.kafka.kms.ui.directory.createDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.name

@Inject
class PrepareEpubUseCase(
    private val directoryRepository: DirectoryRepository,
    private val dispatchers: CoroutineDispatchers
) : Interactor<PrepareEpubUseCase.Params, Unit>() {

    companion object {
        private const val XML_DECLARATION = """<?xml version="1.0" encoding="utf-8"?>"""
    }

    data class Params(val opfPath: String, val xhtmlPath: String)

    override suspend fun doWork(params: Params) {
        return withContext(dispatchers.io) {
            debug { "PrepareEpubUseCase starting with opf: ${params.opfPath}, xhtml: ${params.xhtmlPath}" }
            
            val xhtmlPath = Path(params.xhtmlPath)
            val opfPath = Path(params.opfPath)
            
            // Validate files exist
            require(xhtmlPath.exists()) { "XHTML file not found at ${params.xhtmlPath}" }
            
            // Create repository and directory structure
            val repoId = generateRepoId(xhtmlPath = xhtmlPath)
            debug { "Creating repository with ID: $repoId" }
            directoryRepository.createRepository(repoId)
            
            // Process the files
            processFiles(repoId = repoId, xhtmlPath = xhtmlPath)
            
            // Clean up
            directoryRepository.cleanupDSStoreFiles()
            
            // Create EPUB file
            createEpubFile(repoId, opfPath)
            
            debug { "PrepareEpubUseCase completed for $repoId" }
        }
    }

    private fun generateRepoId(xhtmlPath: Path): String {
        // Use the XHTML filename as the base for the repo ID
        val baseName = xhtmlPath.name.substringBeforeLast(".")
            .lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")
            .replace("\\s+".toRegex(), "_")
        
        return "epub_$baseName"
    }

    private suspend fun processFiles(repoId: String, xhtmlPath: Path) {
        // Create text directory
        val textDirPath = textPath(repoId)
        debug { "Creating text directory at: $textDirPath" }
        
        // Copy XHTML file as body.xhtml
        val bodyPath = createDirectory(textDirPath, "body.xhtml")
        debug { "Copying XHTML to: $bodyPath" }
        withContext(Dispatchers.IO) {
            Files.copy(xhtmlPath, bodyPath.toPath(), StandardCopyOption.REPLACE_EXISTING)
        }
        
        // Split the body.xhtml into chapters
        debug { "Splitting body.xhtml into chapters" }
        GutenbergChapters.splitFile(
            sourceFile = bodyPath.toPath(),
            outputDir = textDirPath.toPath()
        ).onFailure { error ->
            debug { "Failed to split chapters: ${error.message}" }
            throw error
        }
        
        // Clean up chapter files
        debug { "Cleaning up chapter files" }
        cleanupChapterFiles(textDirPath)
        
        // Create placeholder title page
        val titlePageContent = """
            $XML_DECLARATION
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head><title>Title Page</title></head>
            <body>
                <h1>Title Page</h1>
                <p>Generated from: ${xhtmlPath.name}</p>
            </body>
            </html>
        """.trimIndent()
        File(textDirPath, "title.xhtml").writeText(titlePageContent)
        
        // Create placeholder imprint page
        val imprintContent = """
            $XML_DECLARATION
            <!DOCTYPE html>
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head><title>Imprint</title></head>
            <body>
                <h1>Imprint</h1>
                <p>Generated from: ${xhtmlPath.name}</p>
            </body>
            </html>
        """.trimIndent()
        File(textDirPath, "imprint.xhtml").writeText(imprintContent)
        
        // Create TOC file
        createTocFile(textDirPath)

        // Copy and update the OPF file
        val opfDir = DirectoryPaths.epubPath(repoId)
        val opfFile = File(opfDir, "content.opf")
        
        // Copy original OPF file from the same directory as input XHTML
        val originalOpfFile = xhtmlPath.parent.resolve("content.opf")
        if (originalOpfFile.exists()) {
            debug { "Copying original OPF file from ${originalOpfFile}" }
            Files.copy(originalOpfFile, opfFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
            
            // Update the OPF file with chapter information
            updateAndCopyOpfFile(repoId, textDirPath)
        } else {
            debug { "Original OPF file not found at ${originalOpfFile}, skipping update" }
        }
        
        // Clean up the temporary body.xhtml file since we now have chapter files
        bodyPath.delete()
    }

    private fun updateAndCopyOpfFile(repoId: String, textDirPath: File) {
        // Get chapter files
        val chapterFiles = textDirPath.listFiles { file -> 
            file.name.matches(Regex("chapter-\\d+\\.xhtml"))
        }?.sortedBy { file ->
            file.name.replace(Regex("[^0-9]"), "").toInt()
        } ?: return

        // Build manifest items for chapters
        val chapterManifestItems = buildString {
            chapterFiles.forEach { file ->
                val chapterNum = file.name.replace(Regex("[^0-9]"), "")
                append("""
                    |        <item id="chapter-$chapterNum" href="text/${file.name}" media-type="application/xhtml+xml"/>""".trimMargin())
            }
        }

        // Build spine items for chapters
        val chapterSpineItems = buildString {
            chapterFiles.forEach { file ->
                val chapterNum = file.name.replace(Regex("[^0-9]"), "")
                append("""
                    |        <itemref idref="chapter-$chapterNum"/>""".trimMargin())
            }
        }

        // Read the original OPF content
        val opfContent = File(DirectoryPaths.epubPath(repoId), "content.opf").readText()

        // Update the manifest and spine sections
        val updatedContent = opfContent
            .replace(
                Regex("""<item id="pitakepatr" href="text/pitakepatr.xhtml" media-type="application/xhtml+xml"/>"""),
                chapterManifestItems
            )
            .replace(
                Regex("""<itemref idref="pitakepatr"/>"""),
                chapterSpineItems
            )

        // Write the updated OPF file
        File(DirectoryPaths.epubPath(repoId), "content.opf").writeText(updatedContent)
    }

    private fun cleanupChapterFiles(textDirPath: File) {
        textDirPath.listFiles { file -> file.name.matches(Regex("chapter-\\d+\\.xhtml")) }
            ?.forEach { chapterFile ->
                debug { "Cleaning up chapter file: ${chapterFile.name}" }
                
                // Read the content and remove simple div replacements first
                val content = chapterFile.readText()
                    .replace("<div class=\"chapter\">", "")
                    .replace("</div>", "")
                
                // Remove duplicate XML declarations keeping only the first one
                val cleanedContent = if (content.indexOf(XML_DECLARATION) != content.lastIndexOf(XML_DECLARATION)) {
                    val firstPart = content.substring(0, content.indexOf(XML_DECLARATION) + XML_DECLARATION.length)
                    val secondPart = content.substring(content.lastIndexOf(XML_DECLARATION) + XML_DECLARATION.length)
                    firstPart + secondPart
                } else {
                    content
                }
                
                // Parse with KSoup
                val doc = Ksoup.parse(cleanedContent, Parser.xmlParser())
                doc.outputSettings()
                    .syntax(Document.OutputSettings.Syntax.xml)
                    .prettyPrint(true)
                
                // Clean up the document structure
                val body = doc.body()
                
                // Find all section elements
                val sections = body.getElementsByTag("section")
                
                // If we have more than one section at the same level, keep only the first one
                if (sections.size > 1) {
                    val firstSection = sections.first()
                    sections.forEach { section ->
                        if (section != firstSection) {
                            // Move all children to the first section
                            section.children().forEach { child ->
                                firstSection?.appendChild(child)
                            }
                            section.remove()
                        }
                    }
                }
                
                // Remove any end chapter comments
                doc.select("comment").forEach { comment ->
                    if (comment.toString().contains("end chapter")) {
                        comment.remove()
                    }
                }
                
                // Write the cleaned document back to file
                val output = doc.html()
                    .replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&amp;", "&")
                
                chapterFile.writeText(output)
            }
    }

    private fun createTocFile(textDirPath: File) {
        val chapterFiles = textDirPath.listFiles { file -> 
            file.name.matches(Regex("chapter-\\d+\\.xhtml"))
        }?.sortedBy { file ->
            file.name.replace(Regex("[^0-9]"), "").toInt()
        } ?: return

        val chapterEntries = buildString {
            chapterFiles.forEach { file ->
                val chapterTitle = extractChapterTitle(file)
                append("""
                    |            <li>
                    |                <a href="text/${file.name}">$chapterTitle</a>
                    |            </li>
                """.trimMargin())
            }
        }

        val tocContent = buildString {
            // XML declaration must be at the very start with no whitespace
            append(XML_DECLARATION)
            append("\n")
            append("""<html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" epub:prefix="z3998: http://www.daisy.org/z3998/2012/vocab/structure/, se: https://standardebooks.org/vocab/1.0" xml:lang="hi-IN">
    <head>
        <title>Table of Contents</title>
    </head>
    <body epub:type="frontmatter">
        <nav id="toc" epub:type="toc">
            <h2 epub:type="title">Table of Contents</h2>
            <ol>
                <li>
                    <a href="text/title.xhtml">Titlepage</a>
                </li>
                <li>
                    <a href="text/imprint.xhtml">Imprint</a>
                </li>
                $chapterEntries
            </ol>
        </nav>
        <nav id="landmarks" epub:type="landmarks">
            <h2 epub:type="title">Landmarks</h2>
            <ol>
                <li>
                    <a href="text/chapter-1.xhtml" epub:type="bodymatter">Start of Content</a>
                </li>
            </ol>
        </nav>
    </body>
</html>""")
        }

        // Use the existing epub directory
        val epubDir = textDirPath.parentFile
        File(epubDir, "toc.xhtml").writeText(tocContent)
    }

    private fun extractChapterTitle(chapterFile: File): String {
        val content = chapterFile.readText()
        val titleMatch = Regex("<title>(.*?)</title>").find(content)
        return titleMatch?.groupValues?.get(1) 
            ?: "Chapter ${chapterFile.name.replace(Regex("[^0-9]"), "")}"
    }

    private suspend fun createEpubFile(repoId: String, opfPath: Path) = withContext(Dispatchers.IO) {
        val repoPath = DirectoryPaths.repoPath(repoId).toPath()
        val outputDir = repoPath.parent.resolve("output")
        Files.createDirectories(outputDir)
        
        val epubFile = outputDir.resolve("$repoId.epub")
        debug { "Creating EPUB file at: $epubFile" }
        
        // Create ZIP file with mimetype as first entry (uncompressed)
        java.util.zip.ZipOutputStream(Files.newOutputStream(epubFile)).use { zipOut ->
            // Add mimetype first (must be uncompressed)
            val mimetypeEntry = java.util.zip.ZipEntry("mimetype")
            mimetypeEntry.method = java.util.zip.ZipEntry.STORED
            val mimetypeBytes = "application/epub+zip".toByteArray()
            mimetypeEntry.size = mimetypeBytes.size.toLong()
            mimetypeEntry.crc = calculateCRC32(mimetypeBytes)
            zipOut.putNextEntry(mimetypeEntry)
            zipOut.write(mimetypeBytes)
            zipOut.closeEntry()
            
            // Add container.xml
            zipOut.putNextEntry(java.util.zip.ZipEntry("META-INF/container.xml"))
            val containerXml = """<?xml version="1.0" encoding="UTF-8"?>
                |<container version="1.0" xmlns="urn:oasis:names:tc:opendocument:xmlns:container">
                |    <rootfiles>
                |        <rootfile full-path="OEBPS/content.opf" media-type="application/oebps-package+xml"/>
                |    </rootfiles>
                |</container>""".trimMargin()
            zipOut.write(containerXml.toByteArray())
            zipOut.closeEntry()
            
            // Add content.opf - either from existing file or generate new one
            val contentOpf = if (opfPath.exists()) {
                debug { "Using existing OPF file from ${opfPath}" }
                String(Files.readAllBytes(opfPath))
            } else {
                debug { "Generating new OPF file" }
                generateContentOpf(repoId, repoPath.resolve("src"))
            }
            
            zipOut.putNextEntry(java.util.zip.ZipEntry("OEBPS/content.opf"))
            zipOut.write(contentOpf.toByteArray())
            zipOut.closeEntry()
            
            // Add all files from the OEBPS directory
            val oebpsDir = repoPath.resolve("src").resolve("OEBPS")
            Files.walk(oebpsDir).forEach { path ->
                if (Files.isRegularFile(path)) {
                    val relativePath = "OEBPS/" + oebpsDir.relativize(path).toString()
                    zipOut.putNextEntry(java.util.zip.ZipEntry(relativePath))
                    Files.copy(path, zipOut)
                    zipOut.closeEntry()
                }
            }
        }
        
        debug { "EPUB file created successfully" }
    }
    
    private fun generateContentOpf(repoId: String, srcDir: Path): String {
        val textDir = srcDir.resolve("OEBPS").resolve("text")
        
        // Get all chapter files for manifest and spine
        val chapterFiles = Files.list(textDir)
            .filter { it.fileName.toString().matches(Regex("chapter-\\d+\\.xhtml")) }
            .sorted { a, b ->
                val aNum = a.fileName.toString().replace(Regex("[^0-9]"), "").toInt()
                val bNum = b.fileName.toString().replace(Regex("[^0-9]"), "").toInt()
                aNum.compareTo(bNum)
            }
            .toList()

        // Extract title from first chapter
        val firstChapterTitle = chapterFiles.firstOrNull()?.let { file ->
            val content = Files.readString(file)
            Regex("<title>(.*?)</title>").find(content)?.groupValues?.get(1)
        } ?: "पिता के पत्र पुत्री के नाम"

        val manifest = buildString {
            // Add all XHTML files from text directory
            Files.list(textDir).forEach { file ->
                val fileName = file.fileName.toString()
                val id = when {
                    fileName == "title.xhtml" -> "titlepage"
                    fileName == "imprint.xhtml" -> "imprint"
                    fileName.startsWith("chapter-") -> "chapter-${fileName.replace(Regex("[^0-9]"), "")}"
                    else -> fileName.replace(".xhtml", "").replace("-", "_")
                }
                append("""
                    |        <item id="$id" href="text/$fileName" media-type="application/xhtml+xml"/>
                """.trimMargin())
            }
            
            // Add TOC file
            append("""
                |        <item id="toc" href="toc.xhtml" media-type="application/xhtml+xml" properties="nav"/>
            """.trimMargin())
            
            // Add CSS files
            val cssDir = srcDir.resolve("OEBPS").resolve("css")
            if (Files.exists(cssDir)) {
                Files.list(cssDir).forEach { cssFile ->
                    val id = cssFile.fileName.toString().replace(".css", "").replace("-", "_")
                    append("""
                        |        <item id="$id" href="css/${cssFile.fileName}" media-type="text/css"/>
                    """.trimMargin())
                }
            }
            
            // Add image files
            val imagesDir = srcDir.resolve("OEBPS").resolve("images")
            if (Files.exists(imagesDir)) {
                Files.list(imagesDir).forEach { imageFile ->
                    val id = imageFile.fileName.toString().replace(".", "_").replace("-", "_")
                    val mediaType = when (imageFile.fileName.toString().substringAfterLast(".").lowercase()) {
                        "jpg", "jpeg" -> "image/jpeg"
                        "png" -> "image/png"
                        "svg" -> "image/svg+xml"
                        else -> "image/png" // default
                    }
                    append("""
                        |        <item id="$id" href="images/${imageFile.fileName}" media-type="$mediaType"/>
                    """.trimMargin())
                }
            }
        }
        
        val spine = buildString {
            append("""
                |        <itemref idref="titlepage"/>
                |        <itemref idref="imprint"/>
            """.trimMargin())
            
            chapterFiles.forEach { file ->
                val id = "chapter-${file.fileName.toString().replace(Regex("[^0-9]"), "")}"
                append("""
                    |        <itemref idref="$id"/>
                """.trimMargin())
            }
        }
        
        // Format the current time in the required format for dcterms:modified
        val currentTime = java.time.OffsetDateTime.now()
            .format(java.time.format.DateTimeFormatter.ISO_INSTANT)
            .substringBefore(".") + "Z"

        // Generate a unique identifier
        val uuid = java.util.UUID.randomUUID().toString()
        
        return """<?xml version="1.0" encoding="utf-8"?>
            |<package xmlns="http://www.idpf.org/2007/opf" version="3.0" unique-identifier="uid" xmlns:dc="http://purl.org/dc/elements/1.1/" xmlns:dcterms="http://purl.org/dc/terms/" xmlns:opf="http://www.idpf.org/2007/opf">
            |    <metadata>
            |        <dc:identifier id="uid">urn:uuid:$uuid</dc:identifier>
            |        <dc:title>$firstChapterTitle</dc:title>
            |        <dc:creator id="creator">जवाहरलाल नेहरू</dc:creator>
            |        <dc:language>hi-IN</dc:language>
            |        <dc:date>1929-01-01</dc:date>
            |        <dc:publisher>Project Gutenberg</dc:publisher>
            |        <dc:rights>Public domain in the USA.</dc:rights>
            |        <dc:subject>भारतीय इतिहास</dc:subject>
            |        <dc:subject>पारिवारिक पत्र</dc:subject>
            |        <dc:subject>शिक्षाप्रद साहित्य</dc:subject>
            |        <dc:description>जवाहरलाल नेहरू द्वारा अपनी बेटी इंदिरा को लिखे गए पत्रों का संग्रह, जो 1929 में प्रकाशित हुआ था।</dc:description>
            |        <meta property="dcterms:modified">$currentTime</meta>
            |        <meta property="dcterms:type">Text</meta>
            |        <meta property="dcterms:format">application/epub+zip</meta>
            |    </metadata>
            |    <manifest>
            |$manifest
            |    </manifest>
            |    <spine>
            |$spine
            |    </spine>
            |</package>""".trimMargin()
    }
    
    private fun calculateCRC32(data: ByteArray): Long {
        val crc = java.util.zip.CRC32()
        crc.update(data)
        return crc.value
    }
} 
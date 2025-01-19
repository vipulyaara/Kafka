package com.kafka.kms.domain

import com.kafka.base.CoroutineDispatchers
import com.kafka.base.debug
import com.kafka.base.domain.Interactor
import com.kafka.kms.data.files.DirectoryPaths.textPath
import com.kafka.kms.data.files.DirectoryRepository
import com.kafka.kms.domain.gutenberg.GutenbergChapters
import com.kafka.kms.ui.directory.createDirectory
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

    data class Params(
        val opfPath: String,
        val xhtmlPath: String
    )

    override suspend fun doWork(params: Params) {
        return withContext(dispatchers.io) {
            debug { "PrepareEpubUseCase starting with opf: ${params.opfPath}, xhtml: ${params.xhtmlPath}" }
            
            val opfPath = Path(params.opfPath)
            val xhtmlPath = Path(params.xhtmlPath)
            
            // Validate files exist
            require(xhtmlPath.exists()) { "XHTML file not found at ${params.xhtmlPath}" }
            
            // Create repository and directory structure
            val repoId = generateRepoId(opfPath, xhtmlPath)
            debug { "Creating repository with ID: $repoId" }
            directoryRepository.createRepository(repoId)
            
            // Process the files
            processFiles(repoId, opfPath, xhtmlPath)
            
            // Clean up
            directoryRepository.cleanupDSStoreFiles()
            
            debug { "PrepareEpubUseCase completed for $repoId" }
        }
    }

    private fun generateRepoId(opfPath: Path, xhtmlPath: Path): String {
        // Use the XHTML filename as the base for the repo ID
        val baseName = xhtmlPath.name.substringBeforeLast(".")
            .lowercase()
            .replace(Regex("[^a-z0-9\\s]"), "")
            .replace("\\s+".toRegex(), "_")
        
        return "epub_$baseName"
    }

    private suspend fun processFiles(repoId: String, opfPath: Path, xhtmlPath: Path) {
        // Create text directory
        val textDirPath = textPath(repoId)
        debug { "Creating text directory at: $textDirPath" }
        
        // Copy XHTML file as body.xhtml
        val bodyPath = createDirectory(textDirPath, "body.xhtml")
        debug { "Copying XHTML to: $bodyPath" }
        Files.copy(xhtmlPath, bodyPath.toPath(), StandardCopyOption.REPLACE_EXISTING)
        
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
        createTocFile(repoId, textDirPath)
        
        // Clean up the temporary body.xhtml file since we now have chapter files
        bodyPath.delete()
    }

    private fun cleanupChapterFiles(textDirPath: File) {
        textDirPath.listFiles { file -> file.name.matches(Regex("chapter-\\d+\\.xhtml")) }
            ?.forEach { chapterFile ->
                debug { "Cleaning up chapter file: ${chapterFile.name}" }
                var content = chapterFile.readText()
                    .replace("<div class=\"chapter\">", "")
                    .replace("</div>", "")
                
                // Remove unmatched section closing tags
                // Check for any variant of opening section tag
                val hasOpeningSection = content.contains(Regex("<section[^>]*>"))
                if (!hasOpeningSection && content.contains("</section>")) {
                    debug { "Removing unmatched </section> tag in ${chapterFile.name}" }
                    content = content.replace("</section>", "")
                }
                
                // Remove duplicate XML declarations
                val cleanedContent = if (content.indexOf(XML_DECLARATION) != content.lastIndexOf(XML_DECLARATION)) {
                    // Keep only the first occurrence
                    val firstPart = content.substring(0, content.indexOf(XML_DECLARATION) + XML_DECLARATION.length)
                    val secondPart = content.substring(content.lastIndexOf(XML_DECLARATION) + XML_DECLARATION.length)
                    firstPart + secondPart
                } else {
                    content
                }
                
                chapterFile.writeText(cleanedContent)
            }
    }

    private fun createTocFile(repoId: String, textDirPath: File) {
        val chapterFiles = textDirPath.listFiles { file -> 
            file.name.matches(Regex("chapter-\\d+\\.xhtml"))
        }?.sortedBy { file ->
            file.name.replace(Regex("[^0-9]"), "").toInt()
        } ?: return

        val chapterEntries = buildString {
            chapterFiles.forEach { file ->
                val chapterTitle = extractChapterTitle(file)
                append("""
                    |				<li>
                    |					<a href="text/${file.name}">$chapterTitle</a>
                    |				</li>
                """.trimMargin())
            }
        }

        val tocContent = """
            $XML_DECLARATION
            <html xmlns="http://www.w3.org/1999/xhtml" xmlns:epub="http://www.idpf.org/2007/ops" epub:prefix="z3998: http://www.daisy.org/z3998/2012/vocab/structure/, se: https://standardebooks.org/vocab/1.0" xml:lang="en-US">
                <head>
                    <title>Table of Contents</title>
                </head>
                <body epub:type="frontmatter">
                    <nav id="toc" epub:type="toc">
                        <h2 epub:type="title">Table of Contents</h2>
                        <ol>
                            <li>
                                <a href="text/titlepage.xhtml">Titlepage</a>
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
            </html>
        """.trimIndent()

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
} 
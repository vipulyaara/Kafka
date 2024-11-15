package com.kafka.kms.domain.gutenberg

import com.kafka.base.debug
import com.kafka.kms.data.html.convertToXhtml
import com.kafka.kms.ui.directory.createDirectory
import com.kafka.kms.ui.directory.createFile

object CleanGutenbergHtml {
    fun cleanGutenbergHtml(sourceFilePath: String, repoId: String, htmlFileName: String) {
        copyHtmlFile(sourceFilePath, repoId, htmlFileName)
    }

    // convert html file to xhtml and write it to epub/text
    private fun copyHtmlFile(sourceFilePath: String, repoId: String, htmlFileName: String) {
        val baseDir = System.getProperty("user.home")
        val projectPath =
            listOf("StudioProjects", "kms-tools", "ebooks", repoId, "src", "epub", "text")
        val textPath = createDirectory(baseDir, *projectPath.toTypedArray())

        // Handle ZIP file extraction
        val sourceZip = java.util.zip.ZipFile(sourceFilePath)
        val pgHtmlEntry = sourceZip.entries().asSequence()
            .find { it.name.endsWith(htmlFileName) }
            ?: throw IllegalStateException("pg.html not found in ZIP file")

        val targetFile = createFile(textPath, "body.html")
        sourceZip.getInputStream(pgHtmlEntry).use { input ->
            targetFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // Read input file
        val inputText = java.io.File(textPath, "body.html").readText()

        debug { "UpdateGutenbergBook html files $inputText" }

        // Convert HTML to XHTML
        val processedText = convertToXhtml(inputText)
            .replace("<h2", "<!--se:split--><h2")

        debug { "UpdateGutenbergBook html files processed $inputText" }

        // Write to output file
        java.io.File(textPath, "body.xhtml").writeText(processedText)

        sourceZip.close()

        debug { "Extracted pg.html from ZIP and copied to" }
    }
}
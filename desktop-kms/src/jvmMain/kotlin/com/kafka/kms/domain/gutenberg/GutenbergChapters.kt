package com.kafka.kms.domain.gutenberg

import java.io.File
import java.nio.file.Path

object GutenbergChapters {
    private const val DEFAULT_TEMPLATE = "/templates/chapter-template.xhtml"
    
    fun splitFile(
        sourceFile: Path,
        outputDir: Path,
        startAt: Int = 1,
        filenameFormat: String = "chapter-%n.xhtml"
    ): Result<Unit> = runCatching {
        // Read source file
        val sourceContent = sourceFile.toFile().readText()
        
        // Read template
        val templateContent = GutenbergChapters::class.java
            .getResourceAsStream(DEFAULT_TEMPLATE)
            ?.bufferedReader()
            ?.readText() ?: throw IllegalStateException("Template file not found")

        // Detect language
        val language = if (sourceContent.contains(Regex("colour|favour|honour"))) "en-GB" else "en-US"
        
        // Process chapters
        var currentChapter = startAt
        var chapterContent = ""
        
        // Remove leading split tags
        val cleanContent = sourceContent.replace(Regex("^\\s*<!--se:split-->"), "")
        
        cleanContent.lineSequence().forEach { line ->
            if ("<!--se:split-->" in line) {
                val (prefix, suffix) = line.split("<!--se:split-->")
                chapterContent += prefix
                
                // Output current chapter
                outputChapter(
                    outputDir = outputDir,
                    filenameFormat = filenameFormat,
                    chapterNumber = currentChapter,
                    template = templateContent,
                    content = chapterContent.trim(),
                    language = language
                )
                
                currentChapter++
                chapterContent = suffix
            } else {
                chapterContent += "\n$line"
            }
        }
        
        // Output final chapter if there's remaining content
        if (chapterContent.isNotBlank()) {
            outputChapter(
                outputDir = outputDir,
                filenameFormat = filenameFormat,
                chapterNumber = currentChapter,
                template = templateContent,
                content = chapterContent.trim(),
                language = language
            )
        }
    }
    
    private fun outputChapter(
        outputDir: Path,
        filenameFormat: String,
        chapterNumber: Int,
        template: String,
        content: String,
        language: String
    ) {
        val filename = filenameFormat.replace("%n", chapterNumber.toString())
        val id = filename.replace(Regex("\\.xhtml$"), "")
        
        var xhtml = template
            .replace("LANG", language)
            .replace("ID", id)
            .replace("NUMERAL", toRoman(chapterNumber))
            .replace("NUMBER", chapterNumber.toString())
            .replace("TEXT", content)
            
        outputDir.toFile().mkdirs()
        File(outputDir.toFile(), filename).writeText(xhtml)
    }
    
    private fun toRoman(number: Int): String {
        val romanNumerals = listOf(
            1000 to "M",
            900 to "CM",
            500 to "D",
            400 to "CD",
            100 to "C",
            90 to "XC",
            50 to "L",
            40 to "XL",
            10 to "X",
            9 to "IX",
            5 to "V",
            4 to "IV",
            1 to "I"
        )
        
        var remaining = number
        return buildString {
            for ((value, numeral) in romanNumerals) {
                while (remaining >= value) {
                    append(numeral)
                    remaining -= value
                }
            }
        }
    }
}
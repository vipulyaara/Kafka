package com.kafka.kms.ui.directory

import java.io.File

fun createDirectory(basePath: String, vararg directories: String): String {
    directories.forEach { dir ->
        val path = File(basePath, dir)
        if (!path.exists()) {
            path.mkdirs()
        }
    }
    return joinPath(basePath, *directories)
}

// New utility functions
fun createFile(path: String, fileName: String) = File(path, fileName)

fun createFileWithPath(fullPath: String, content: String) {
    val file = File(fullPath)
    file.writeText(content)
}

fun joinPath(basePath: String, vararg paths: String): String {
    return basePath + "/" + paths.joinToString("/")
}

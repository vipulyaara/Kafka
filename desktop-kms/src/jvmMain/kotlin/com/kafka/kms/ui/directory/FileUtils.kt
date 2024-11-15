package com.kafka.kms.ui.directory

import java.io.File

//fun createDirectory(basePath: String, vararg directories: String): String {
//    directories.forEach { dir ->
//        val path = File(basePath, dir)
//        if (!path.exists()) {
//            path.mkdirs()
//        }
//    }
//    return joinPath(basePath, *directories)
//}

fun createDirectory(basePath: String, vararg paths: String): File {
    return File(basePath, paths.joinToString(File.separator)).apply {
        if (!exists()) {
            mkdirs()
        }
    }
}

// Overload for when the base path is already a File
fun createDirectory(basePath: File, vararg paths: String): File {
    return File(basePath, paths.joinToString(File.separator)).apply {
        if (!exists()) {
            mkdirs()
        }
    }
}

fun createFile(directory: File, fileName: String): File {
    return File(directory, fileName).apply {
        parentFile.mkdirs()
        createNewFile()
    }
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

package com.kafka.data.platform

import java.io.File

val appDirectory: String
    get() {
        val appName = "kafka"
        val appDirectory: String = when (System.getProperty("os.name").lowercase()) {
            "windows" -> "${System.getenv("LOCALAPPDATA")}/$appName"
            "mac os x", "darwin" -> "${System.getProperty("user.home")}/Library/Application Support/$appName"
            else -> System.getProperty("os.name")
        }

        val dbDirFile = File(appDirectory)
        if (!dbDirFile.exists()) {
            dbDirFile.mkdirs()
        }

        return appDirectory
    }

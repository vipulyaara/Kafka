package com.kafka.downloader.core

interface Downloader {
    fun download(fileId: String)
    fun retry(fileId: String)
    fun remove(fileId: String)
}

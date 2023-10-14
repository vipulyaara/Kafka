package org.kafka.navigation.deeplink

object Config {
    private const val BASE_HOST = "kafka-books.web.app"
    const val BASE_URL = "https://$BASE_HOST/"

    fun archiveDetailUrl(itemId: String) = "https://archive.org/details/$itemId"
}
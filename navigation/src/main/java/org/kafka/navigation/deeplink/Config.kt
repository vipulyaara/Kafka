package org.kafka.navigation.deeplink

object Config {
    const val BASE_URL = "https://kafkaarchives.org/"
    const val BASE_URL_ALT = "https://kafka-books.web.app/"
    const val PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.kafka.user"

    fun archiveDetailUrl(itemId: String) = "https://archive.org/details/$itemId"
}

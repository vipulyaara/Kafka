package org.kafka.base

interface SearchContent {
    data class Message(val message: String) : SearchContent
}

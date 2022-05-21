package org.rekhta.base

interface SearchContent {
    data class Message(val message: String) : SearchContent
}

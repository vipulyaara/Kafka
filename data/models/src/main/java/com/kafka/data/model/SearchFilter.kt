package com.kafka.data.model

import org.kafka.base.debug

enum class SearchFilter {
    Name, Creator, Subject;

    companion object {
        private fun fromString(value: String): SearchFilter {
            debug { "SearchFilter.fromString: $value" }
            return values().first { it.name.lowercase() == value.lowercase() }
        }

        fun from(value: String): List<SearchFilter> {
            debug { "SearchFilter.fromAll: $value" }
            return value.split(",").map { SearchFilter.fromString(it.trim()) }
        }

        fun allString(): String {
            return values().toList().joinToString(",") { it.name }
        }

        fun all() = values().toList()
    }
}


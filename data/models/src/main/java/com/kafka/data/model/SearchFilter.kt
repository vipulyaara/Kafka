package com.kafka.data.model

import org.kafka.base.debug

enum class SearchFilter {
    Name, Creator, Subject;

    companion object {
        private fun fromString(value: String): SearchFilter {
            debug { "SearchFilter.fromString: $value" }
            return values().firstOrNull { it.name.lowercase() == value.lowercase() } ?: Name
        }

        fun from(value: String): List<SearchFilter> {
            debug { "SearchFilter.fromAll: $value" }
            return value.split(",").map { SearchFilter.fromString(it.trim()) }
        }

        fun toString(filters: List<SearchFilter>): String {
            return filters.joinToString(",") { it.name }
        }

        fun allString(): String {
            return values().toList().joinToString(",") { it.name }
        }

        fun all() = values().toList()
    }
}

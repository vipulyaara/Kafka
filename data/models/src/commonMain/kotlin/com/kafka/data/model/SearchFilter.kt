package com.kafka.data.model

import com.kafka.base.debug

enum class SearchFilter {
    Name, Creator, Subject;

    companion object {
        private fun fromString(value: String): SearchFilter {
            debug { "SearchFilter.fromString: $value" }
            return entries.firstOrNull { it.name.lowercase() == value.lowercase() } ?: Name
        }

        fun from(value: String): List<SearchFilter> {
            debug { "SearchFilter.fromAll: $value" }
            return value.split(",").map { SearchFilter.fromString(it.trim()) }
        }

        fun toString(filters: List<SearchFilter>): String {
            return filters.joinToString(",") { it.name }
        }

        fun allString(): String {
            return entries.joinToString(",") { it.name }
        }

        fun all() = entries
    }
}

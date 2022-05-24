package org.kafka.base

fun String?.takeIfNotEmpty() = if (this.isNullOrBlank()) null else this

fun String?.orNa() = this ?: "N/A"

fun <X, Y> Map<X, Y?>.filterNonNullValues(): Map<X, Y> = filterValues { it != null } as Map<X, Y>

fun listOfNonEmpty(vararg elements: String?): List<String> =
    elements.filterNotNull().filter { it.isNotEmpty() }

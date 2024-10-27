package com.kafka.data.entities

import kotlinx.collections.immutable.persistentListOf

data class Homepage(val collection: List<HomepageCollection>) {
    val hasSearchPrompt: Boolean
        get() = collection.isNotEmpty()

    companion object {
        val Empty = Homepage(persistentListOf())
    }
}

val List<HomepageCollection>.recentItems
    get() = filterIsInstance<HomepageCollection.RecentItems>()
        .firstOrNull()?.items.orEmpty()

sealed class HomepageCollection {
    data class FeaturedItem(
        val label: String?,
        val items: List<Item>,
        val image: List<String> = persistentListOf(),
        val shuffle: Boolean,
    ) : HomepageCollection()

    data class RecentItems(
        val items: List<RecentItem>,
    ) : HomepageCollection()

    data class Recommendations(
        val labels: List<String>,
        val type: String,
        val items: List<Item>,
    ) : HomepageCollection()

    data class PersonRow(
        val items: List<String>,
        val images: List<String>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
    ) : HomepageCollection()

    data class Row(
        val labels: List<String>,
        val items: List<Item>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }

    data class Column(
        val labels: List<String>,
        val items: List<Item>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }

    data class Grid(
        val labels: List<String>,
        val items: List<Item>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
    ) : HomepageCollection() {
        val key = labels.joinToString(separator = ",")
    }

    data class Subjects(
        val items: List<String>,
        val clickable: Boolean = true,
        val shuffle: Boolean,
    ) : HomepageCollection() {
        val key = items.joinToString(separator = ",")
    }
}

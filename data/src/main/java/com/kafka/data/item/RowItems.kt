package com.kafka.data.item

import com.kafka.data.entities.Item
import com.kafka.data.query.ArchiveQuery

class RowItems : HashMap<ArchiveQuery, List<Item>>() {
    fun add(archiveQuery: ArchiveQuery, items: List<Item>): RowItems {
        put(archiveQuery, items)
        return this
    }
}

fun RowItems.sortByPosition(): RowItems {
    return RowItems().also { it.putAll(toList().sortedBy { it.first.position }.toMap()) }
}



package com.kafka.data.item

import com.kafka.data.entities.Item
import com.kafka.data.query.ArchiveQuery

class RowItems : HashMap<ArchiveQuery, RowItem>() {
    fun add(archiveQuery: ArchiveQuery, items: List<Item>): RowItems {
        put(archiveQuery, RowItem(items))
        return this
    }
}

data class RowItem(val items: List<Item>)

fun RowItems.sortByPosition(): RowItems {
    return RowItems().also { it.putAll(toList().sortedBy { it.first.position }.toMap()) }
}



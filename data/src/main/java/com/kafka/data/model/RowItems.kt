package com.kafka.data.model

import com.data.base.model.ArchiveQuery
import com.kafka.data.entities.Item

class RowItems : HashMap<ArchiveQuery, RowItem>() {
    fun add(archiveQuery: ArchiveQuery, items: List<Item>): RowItems {
        put(archiveQuery, RowItem(items))
        return this
    }
}

data class RowItem(val items: List<Item>)

fun RowItems.sortByPosition(): RowItems {
    return RowItems()
        .also { it.putAll(toList().sortedBy { it.first.position }.toMap()) }
}



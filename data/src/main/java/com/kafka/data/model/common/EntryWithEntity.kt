package com.kafka.data.model.common

import java.util.Objects

interface EntryWithEntity<ET : Entry, EN : BaseEntity> {
    var entry: ET?
    var relations: List<EN>

    val entity: EN
        get() {
            assert(relations.size == 1)
            return relations[0]
        }

    fun generateStableId(): Long {
        return Objects.hash(entry!!::class, entity.id).toLong()
    }
}

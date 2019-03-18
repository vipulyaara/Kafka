package com.kafka.data.model

import com.kafka.data.entities.Item

/**
 * @author Vipul Kumar; dated 14/02/19.
 */
data class RailItem(
    val title: String = "",
    val items: List<Item>?
)

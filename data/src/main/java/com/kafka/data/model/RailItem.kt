package com.kafka.data.model

import com.kafka.data.entities.Content

/**
 * @author Vipul Kumar; dated 14/02/19.
 */
data class RailItem(
    val title: String = "",
    val contents: List<Content>?
)

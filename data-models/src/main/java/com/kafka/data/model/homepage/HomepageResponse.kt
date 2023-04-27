package com.kafka.data.model.homepage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class HomepageCollectionResponse {
    abstract val label: String
    abstract val items: List<String>
    abstract val labelClickable: Boolean

    @Serializable
    @SerialName("row")
    data class Row(
        override val label: String,
        override val items: List<String>,
        override val labelClickable: Boolean = true
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("column")
    data class Column(
        override val label: String,
        override val items: List<String>,
        override val labelClickable: Boolean = true
    ) : HomepageCollectionResponse()
}

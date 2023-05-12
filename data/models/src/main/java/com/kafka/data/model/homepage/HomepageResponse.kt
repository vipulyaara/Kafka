package com.kafka.data.model.homepage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class HomepageCollectionResponse {
    abstract val label: String
    abstract val items: String
    abstract val labelClickable: Boolean
    abstract val enabled: Boolean
    abstract val index: Int

    @Serializable
    @SerialName("row")
    data class Row(
        override val label: String,
        override val items: String,
        override val labelClickable: Boolean = true,
        override val enabled: Boolean = true,
        override val index: Int = 0
    ) : HomepageCollectionResponse()

    @Serializable
    @SerialName("column")
    data class Column(
        override val label: String,
        override val items: String,
        override val labelClickable: Boolean = true,
        override val enabled: Boolean = true,
        override val index: Int = 0
    ) : HomepageCollectionResponse()
}

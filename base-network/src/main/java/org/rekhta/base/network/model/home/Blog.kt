package org.rekhta.base.network.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Blog(
    @SerialName("I") var id: String? = null,
    @SerialName("TU") var targetUrl: String? = null,
    @SerialName("IU") var imageUrl: String? = null,
)

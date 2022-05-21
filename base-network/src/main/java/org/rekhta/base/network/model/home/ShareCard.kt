package org.rekhta.base.network.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ShareCard(
    @SerialName("I") var id: String? = null,
    @SerialName("ST") var shareTitle: String? = null,
    @SerialName("SI") var si: String? = null,
    @SerialName("IU") var imageUrl: String? = null,
    @SerialName("ATU") var atu: String? = null,
    @SerialName("ITU") var itu: String? = null,
)

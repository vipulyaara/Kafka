package org.rekhta.base.network.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomepageResponseContainer(
    @SerialName("S") var s: Int?,
    @SerialName("R") var response: HomepageResponse?,
    @SerialName("T") var t: String?
)

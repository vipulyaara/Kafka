package org.rekhta.base.network.model.poet.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PoetDetailResponseContainer(
    @SerialName("Me")
    val me: String? = null,
    @SerialName("Mh")
    val mh: String? = null,
    @SerialName("Mu")
    val mu: String? = null,
    @SerialName("R")
    val poetResponse: PoetResponse? = null,
    @SerialName("S")
    val s: Int? = null,
    @SerialName("T")
    val t: String? = null
)

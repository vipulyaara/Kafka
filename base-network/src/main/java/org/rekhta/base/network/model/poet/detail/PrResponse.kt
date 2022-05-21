package org.rekhta.base.network.model.poet.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrResponse(
    @SerialName("DR")
    val dR: String?,
    @SerialName("EN")
    val eN: String,
    @SerialName("ER")
    val eR: String,
    @SerialName("ERI")
    val eRI: String,
    @SerialName("EU")
    val eU: String?,
    @SerialName("HI")
    val hI: Boolean,
    @SerialName("IU")
    val iU: String?,
    @SerialName("SS")
    val sS: String
)

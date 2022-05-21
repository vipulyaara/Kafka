package org.rekhta.base.network.model.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionTypeResponseContainer(
    @SerialName("Me")
    val me: String?,
    @SerialName("Mh")
    val mh: String?,
    @SerialName("Mu")
    val mu: String?,
    @SerialName("R")
    val r: R,
    @SerialName("S")
    val s: Int,
    @SerialName("T")
    val t: String
) {
    @Serializable
    data class R(
        @SerialName("CT")
        val cT: List<CT>
    ) {
        @Serializable
        data class CT(
            @SerialName("CS")
            val cS: Int,
            @SerialName("NE")
            val nE: String,
            @SerialName("NH")
            val nH: String,
            @SerialName("NU")
            val nU: String,
            @SerialName("TI")
            val tI: String,
            @SerialName("TS")
            val tS: String
        )
    }
}

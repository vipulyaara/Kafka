package org.rekhta.base.network.model.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CollectionListResponseContainer(
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
        @SerialName("CL")
        val cL: List<CL>,
        @SerialName("DE")
        val dE: String
    ) {
        @Serializable
        data class CL(
            @SerialName("CC")
            val cC: String,
            @SerialName("CLT")
            val cLT: String,
            @SerialName("CN")
            val cN: String,
            @SerialName("CS")
            val cS: Int,
            @SerialName("ET")
            val eT: String?,
            @SerialName("FC")
            val fC: Int,
            @SerialName("I")
            val i: String,
            @SerialName("IU")
            val iU: String,
            @SerialName("PI")
            val pI: String,
            @SerialName("S")
            val s: String,
            @SerialName("SC")
            val sC: Int,
            @SerialName("TI")
            val tI: String,
            @SerialName("TN")
            val tN: String,
            @SerialName("TS")
            val tS: String,
            @SerialName("UE")
            val uE: String,
            @SerialName("UH")
            val uH: String,
            @SerialName("UU")
            val uU: String
        )
    }
}

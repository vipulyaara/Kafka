package org.rekhta.base.network.model.poet.detail

import kotlinx.serialization.Serializable

@Serializable
data class PoetResponse(
    @kotlinx.serialization.SerialName("CH")
    val cH: CH? = null,
    @kotlinx.serialization.SerialName("CS")
    val cS: List<CS>? = null,
    @kotlinx.serialization.SerialName("EP")
    val eP: PoetProfile? = null,
    @kotlinx.serialization.SerialName("PR")
    val pR: List<PrResponse>? = null,
    @kotlinx.serialization.SerialName("SS")
    val sS: SS? = null,
    @kotlinx.serialization.SerialName("UL")
    val uL: List<UL>? = null
)

@Serializable
data class SS(@kotlinx.serialization.SerialName("CR") val cr: String)

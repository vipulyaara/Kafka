package org.rekhta.base.network.model.poet.detail

import kotlinx.serialization.Serializable

@Serializable
data class PoetProfile(
    @kotlinx.serialization.SerialName("AC")
    val aC: Int? = null,
    @kotlinx.serialization.SerialName("AI")
    val aI: String? = null,
    @kotlinx.serialization.SerialName("BP")
    val bP: String? = null,
    @kotlinx.serialization.SerialName("DOB")
    val dOB: String? = null,
    @kotlinx.serialization.SerialName("DOD")
    val dOD: String? = null,
    @kotlinx.serialization.SerialName("DP")
    val dP: String? = null,
    @kotlinx.serialization.SerialName("EB")
    val eB: String? = null,
    @kotlinx.serialization.SerialName("EC")
    val eC: Int? = null,
    @kotlinx.serialization.SerialName("ED")
    val eD: String? = null,
    @kotlinx.serialization.SerialName("HI")
    val hI: Boolean? = null,
    @kotlinx.serialization.SerialName("HPE")
    val hPE: Boolean? = null,
    @kotlinx.serialization.SerialName("HPH")
    val hPH: Boolean? = null,
    @kotlinx.serialization.SerialName("HPU")
    val hPU: Boolean? = null,
    @kotlinx.serialization.SerialName("I")
    val i: String? = null,
    @kotlinx.serialization.SerialName("ISC")
    val iSC: Int? = null,
    @kotlinx.serialization.SerialName("NE")
    val nE: String? = null,
    @kotlinx.serialization.SerialName("PE")
    val pE: String? = null,
    @kotlinx.serialization.SerialName("PN")
    val pN: String? = null,
    @kotlinx.serialization.SerialName("PS")
    val pS: String? = null,
    @kotlinx.serialization.SerialName("RN")
    val rN: String? = null,
    @kotlinx.serialization.SerialName("S")
    val s: String? = null,
    @kotlinx.serialization.SerialName("SP")
    val sP: String? = null,
    @kotlinx.serialization.SerialName("VC")
    val vC: Int? = null
) {
    val lifeSpan: String
        get() = listOfNotNull(dOB, dOD).joinToString(" - ")

    val subTitle
        get() = listOfNotNull(lifeSpan, dP).filter { it.isNotEmpty() }.joinToString(" • ")
}

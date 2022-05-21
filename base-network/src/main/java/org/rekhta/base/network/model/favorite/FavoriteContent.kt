package org.rekhta.base.network.model.favorite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteContent(
    @SerialName("AC")
    val aC: String?,
    @SerialName("AU")
    val aU: String?,
    @SerialName("BE")
    val bE: String,
    @SerialName("BH")
    val bH: String,
    @SerialName("BU")
    val bU: String,
    @SerialName("EC")
    val eC: Boolean,
    @SerialName("FC")
    val fC: String?,
    @SerialName("FD")
    val fD: String,
    @SerialName("FL")
    val fL: Int,
    @SerialName("FM")
    val fM: String,
    @SerialName("FS")
    val fS: Boolean,
    @SerialName("FTE")
    val fTE: String,
    @SerialName("FTH")
    val fTH: String,
    @SerialName("FTU")
    val fTU: String,
    @SerialName("HE")
    val hE: Boolean,
    @SerialName("HFE")
    val hFE: Boolean,
    @SerialName("HFH")
    val hFH: Boolean,
    @SerialName("HFU")
    val hFU: Boolean,
    @SerialName("HH")
    val hH: Boolean,
    @SerialName("HU")
    val hU: Boolean,
    @SerialName("I")
    val i: String,
    @SerialName("N")
    val n: Boolean,
    @SerialName("P")
    val p: String?,
    @SerialName("PC")
    val pC: Boolean,
    @SerialName("PE")
    val pE: String,
    @SerialName("PH")
    val pH: String,
    @SerialName("PI")
    val pI: String,
    @SerialName("PP")
    val pP: String?,
    @SerialName("PU")
    val pU: String,
    @SerialName("R")
    val r: String,
    @SerialName("S")
    val s: String,
    @SerialName("SC")
    val sC: String?,
    @SerialName("SE")
    val sE: String,
    @SerialName("SH")
    val sH: String,
    @SerialName("SI")
    val sI: Int,
    @SerialName("SU")
    val sU: String,
    @SerialName("T")
    val t: String,
    @SerialName("TE")
    val tE: String,
    @SerialName("TH")
    val tH: String,
    @SerialName("TU")
    val tU: String,
    @SerialName("UE")
    val uE: String,
    @SerialName("UH")
    val uH: String,
    @SerialName("UU")
    val uU: String,
    @SerialName("VC")
    val vC: String?,
    @SerialName("VI")
    val vI: String?
)

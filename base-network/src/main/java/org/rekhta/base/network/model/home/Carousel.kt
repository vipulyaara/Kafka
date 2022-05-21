package org.rekhta.base.network.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Carousel(
    @SerialName("I") var i: String? = null,
    @SerialName("BI") var bI: String? = null,
    @SerialName("BE") var bE: String? = null,
    @SerialName("LT") var lT: String? = null,
    @SerialName("BL") var bL: String? = null,
    @SerialName("FD") var fD: String? = null,
    @SerialName("TD") var tD: String? = null,
    @SerialName("TI") var tI: String? = null,
    @SerialName("TU") var tU: String? = null,
    @SerialName("CI") var cI: String? = null,
    @SerialName("IT") var iT: Int? = null,
    @SerialName("S") var s: Int? = null,
    @SerialName("BT") var bT: Int? = null,
    @SerialName("TIN") var tIN: Int? = null,
    @SerialName("MP") var mP: Boolean? = null,
    @SerialName("LB") var lB: String? = null,
    @SerialName("LC") var lC: String? = null,
    @SerialName("PN") var pN: String? = null,
    @SerialName("IU") var iU: String? = null
) {
    val imageUrl
        get() = iU

//    fun getText(language: Language) = when (language) {
//        Language.English -> be?.toLowerCase()?.capitalize()
//        Language.Hindi -> bh
//        Language.Urdu -> bh
//    }
//
//    fun getSubText(language: Language) = when (language) {
//        Language.English -> ble
//        Language.Hindi -> blh
//        Language.Urdu -> blu
//    }
//
}

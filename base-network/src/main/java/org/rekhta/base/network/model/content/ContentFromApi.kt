package org.rekhta.base.network.model.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.Language
import org.rekhta.base.takeIfNotEmpty

@Serializable
class ContentFromApi {
    @SerialName("I")
    var i: String? = null

    @SerialName("T")
    var t: String? = null

    @SerialName("PI")
    var pI: String? = null

    @SerialName("PE")
    var pE: String? = null

    @SerialName("PH")
    var pH: String? = null

    @SerialName("PU")
    var pU: String? = null

    @SerialName("P")
    var p: String? = null

    @SerialName("TE")
    var tE: String? = null

    @SerialName("TH")
    var tH: String? = null

    @SerialName("TU")
    var tU: String? = null

    @SerialName("SE")
    var sE: String? = null

    @SerialName("SH")
    var sH: String? = null

    @SerialName("SU")
    var sU: String? = null

    @SerialName("BE")
    var bE: String? = null

    @SerialName("BH")
    var bH: String? = null

    @SerialName("BU")
    var bU: String? = null

    @SerialName("R")
    var r: String? = null

    @SerialName("S")
    var s: String? = null

    @SerialName("SI")
    var sI: Int? = null

    @SerialName("N")
    var n: Boolean? = null

    @SerialName("EC")
    var eC: Boolean? = null

    @SerialName("PC")
    var pC: Boolean? = null

    @SerialName("AU")
    var aU: Boolean? = null

    @SerialName("VI")
    var vI: Boolean? = null

    @SerialName("AC")
    var aC: Int? = null

    @SerialName("VC")
    var vC: Int? = null

    @SerialName("HE")
    var hE: Boolean? = null

    @SerialName("HH")
    var hH: Boolean? = null

    @SerialName("HU")
    var hU: Boolean? = null

    @SerialName("UE")
    var uE: String? = null

    @SerialName("UH")
    var uH: String? = null

    @SerialName("UU")
    var uU: String? = null

    @SerialName("FC")
    var fC: Int? = null

    @SerialName("SC")
    var sC: Int? = null

    @SerialName("PP")
    var pP: Float? = null

    @SerialName("TS")
    var tS: List<T>? = null

    @SerialName("RE")
    var rE: String? = null

    @SerialName("RH")
    var rH: String? = null

    @SerialName("RU")
    var rU: String? = null

    @SerialName("A")
    var a: Int? = null

    @SerialName("IH")
    var iH: Boolean? = null

    @SerialName("HT")
    var hT: Boolean? = null

    @SerialName("FTE")
    var fTE: String? = null

    @SerialName("FTH")
    var fTH: String? = null

    @SerialName("FTU")
    var fTU: String? = null

    @SerialName("HFE")
    var hFE: Boolean? = null

    @SerialName("HFH")
    var hFH: Boolean? = null

    @SerialName("HFU")
    var hFU: Boolean? = null

    fun getPoetName(currentLanguage: Language) = when (currentLanguage) {
        Language.English -> pE.takeIfNotEmpty() ?: pH.takeIfNotEmpty() ?: pU.takeIfNotEmpty()
        Language.Hindi -> pH.takeIfNotEmpty() ?: pE.takeIfNotEmpty() ?: pU.takeIfNotEmpty()
        Language.Urdu -> pU.takeIfNotEmpty() ?: pE.takeIfNotEmpty() ?: pH.takeIfNotEmpty()
    }

    fun getContentText(currentLanguage: Language) = when (currentLanguage) {
        Language.English -> sE.takeIfNotEmpty() ?: sH.takeIfNotEmpty() ?: sU.takeIfNotEmpty()
        Language.Hindi -> sH.takeIfNotEmpty() ?: sE.takeIfNotEmpty() ?: sU.takeIfNotEmpty()
        Language.Urdu -> sU.takeIfNotEmpty() ?: sE.takeIfNotEmpty() ?: sH.takeIfNotEmpty()
    }?.trim().orEmpty()

    fun getContentTitle(currentLanguage: Language) = when (currentLanguage) {
        Language.English -> tE.takeIfNotEmpty() ?: tH.takeIfNotEmpty() ?: tU.takeIfNotEmpty()
        Language.Hindi -> tH.takeIfNotEmpty() ?: tE.takeIfNotEmpty() ?: tU.takeIfNotEmpty()
        Language.Urdu -> tU.takeIfNotEmpty() ?: tE.takeIfNotEmpty() ?: tH.takeIfNotEmpty()
    }?.trim().orEmpty()

//    fun getContentTitle(currentLanguage: Language) = Language.languageFallbackOrder(currentLanguage)
//        .map { getContentTitleForLanguage(it).takeIfNotEmpty() }.firstOrNull()

    private fun getContentTitleForLanguage(currentLanguage: Language) = when (currentLanguage) {
        Language.English -> tE
        Language.Hindi -> tH
        Language.Urdu -> tU
    }?.trim()
}

package org.rekhta.base.network.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.content.ContentToRender

@Serializable
class WordOfTheDay {
    @SerialName("RF")
    var rF: Int? = null

    @SerialName("WE")
    var wE: String? = null

    @SerialName("WH")
    var wH: String? = null

    @SerialName("WU")
    var wU: String? = null

    @SerialName("WM")
    var wM: String? = null

    @SerialName("T")
    var t: String? = null

    @SerialName("PI")
    var pI: String? = null

    @SerialName("PN")
    var pN: String? = null

    @SerialName("PS")
    var pS: String? = null

    @SerialName("PT")
    var pT: String? = null

    @SerialName("CPS")
    var cPS: String? = null

    @SerialName("PTS")
    var pTS: String? = null

    @SerialName("DI")
    var dI: String? = null

    @SerialName("HA")
    var hA: Boolean? = null

    @SerialName("AMF")
    var aMF: String? = null

    @SerialName("AOF")
    var aOF: String? = null

    var contentToRender: ContentToRender? = null

    val isValid: Boolean
        get() = wE != null
}

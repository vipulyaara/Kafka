package org.rekhta.base.network.model.home

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.content.ContentToRender
import org.rekhta.base.network.model.content.textWithExtras

@Serializable
@Immutable
class TodaysTop {
    @SerialName("CI")
    var cI: String? = null

    @SerialName("S")
    var s: String? = null

    @SerialName("SI")
    var sI: Int? = null

    @SerialName("T")
    var t: String? = null

    @SerialName("TR")
    var tR: String? = null

    @SerialName("RA")
    var rA: Int? = null

    @SerialName("RF")
    var rF: Int? = null

    @SerialName("TS")
    var tS: String? = null

    @SerialName("TN")
    var tN: String? = null

    @SerialName("PI")
    var pI: String? = null

    @SerialName("PN")
    var pN: String? = null

    @SerialName("PS")
    var pS: String? = null

    @SerialName("PDS")
    var pDS: String? = null

    @SerialName("CPS")
    var cPS: String? = null

    @SerialName("PTS")
    var pTS: String? = null

    @SerialName("UE")
    var uE: String? = null

    @SerialName("UH")
    var uH: String? = null

    @SerialName("UU")
    var uU: String? = null

    @SerialName("TG")
    var tG: List<TG>? = null

    @SerialName("PT")
    var pT: String? = null

    @SerialName("NT")
    var nT: String? = null

    var contentUrl: String? = null
    var contentToRender: ContentToRender? = null
    val textWithExtras: String
        get() = contentToRender?.textWithExtras(pN.orEmpty(), contentUrl.orEmpty()).orEmpty()
}

@Serializable
class TG {
    @SerialName("TI")
    var tI: String? = null

    @SerialName("TN")
    var tN: String? = null

    @SerialName("TS")
    var tS: String? = null

    @SerialName("CI")
    var cI: String? = null
}

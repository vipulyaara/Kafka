package org.rekhta.base.network.model.contentdetail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.content.ContentToRender
import org.rekhta.base.network.model.content.Tag
import org.rekhta.base.network.model.home.Video

@Serializable
class ContentDetailResponseContainer {
    @SerialName("S")
    var s: Int? = null

    @SerialName("Me")
    var errorEnglish: String? = null

    @SerialName("R")
    var contentDetailResponse: ContentDetailResponse? = null

    @SerialName("T")
    var t: String? = null
}

@Serializable
class ContentDetailResponse {
    @SerialName("NF")
    var nF: Boolean? = null

    @SerialName("AS")
    var aS: String? = null

    @SerialName("ATS")
    var aTS: String? = null

    @SerialName("EC")
    var eC: Boolean? = null

    @SerialName("PC")
    var pC: Boolean? = null

    @SerialName("I")
    var i: String? = null

    @SerialName("SI")
    var sI: Int? = null

    @SerialName("CS")
    var cS: String? = null

    @SerialName("CT")
    var cT: String? = null

    @SerialName("ST")
    var sT: String? = null

    @SerialName("FT")
    var fT: String? = null

    @SerialName("HN")
    var hN: Boolean? = null

    @SerialName("HH")
    var hH: Boolean? = null

    @SerialName("HU")
    var hU: Boolean? = null

    @SerialName("HT")
    var hT: Boolean? = null

    @SerialName("RF")
    var rF: Int? = null

    @SerialName("RA")
    var rA: Int? = null

    @SerialName("CTN")
    var cTN: String? = null

    @SerialName("TS")
    var tS: String? = null

    @SerialName("CR")
    var cR: String? = null

    @SerialName("RFP")
    var rFP: String? = null

    @SerialName("CTS")
    var cTS: String? = null

    @SerialName("TD")
    var tD: String? = null

    @SerialName("TN")
    var tN: String? = null

    @SerialName("PT")
    var pT: String? = null

    @SerialName("PS")
    var pS: String? = null

    @SerialName("PTS")
    var pTS: String? = null

    @SerialName("UE")
    var uE: String? = null

    @SerialName("UH")
    var uH: String? = null

    @SerialName("UU")
    var uU: String? = null

    @SerialName("Videos")
    var videos: List<Video>? = null

    @SerialName("Audios")
    var audios: List<Audio>? = null

    @SerialName("FPMappings")
    var fPMappings: List<FpMapping>? = null

    @SerialName("FPParaMappings")
    var fPParaMappings: List<FppMapping>? = null

    @SerialName("Tags")
    var tags: List<Tag>? = null

    @SerialName("Poet")
    var poet: Poet? = null

    @SerialName("ParaInfo")
    var paraInfo: List<ParaInfo>? = null

    @SerialName("IH")
    var iH: String? = null

    @SerialName("FC")
    var fC: String? = null

    @SerialName("SC")
    var sC: String? = null

    var contentToRender: ContentToRender? = null
}

@Serializable
data class FpMapping(
    @SerialName("I") var i: Int? = null,
    @SerialName("PC") var pc: Int? = null
)

@Serializable
data class FppMapping(
    @SerialName("IE") var ie: Boolean? = null,
    @SerialName("PN") var pn: Int? = null
)

@Serializable
data class ParaInfo(
    @SerialName("PI") var pI: String? = null,
    @SerialName("PN") var pN: String? = null
)

@Serializable
class Poet {
    @SerialName("CS")
    var cS: String? = null

    @SerialName("DS")
    var dS: String? = null

    @SerialName("PN")
    var pN: String? = null

    @SerialName("IU")
    var iU: String? = null

    @SerialName("LI")
    var lI: Boolean? = null

    @SerialName("PI")
    var pI: String? = null
}

@Serializable
class Audio {
    @SerialName("I")
    var i: String? = null

    @SerialName("SQ")
    var sQ: Int? = null

    @SerialName("AN")
    var aN: String? = null

    @SerialName("HI")
    var hI: Boolean? = null

    @SerialName("ASS")
    var aSS: String? = null

    @SerialName("ADS")
    var aDS: String? = null

    @SerialName("IU")
    var iU: String? = null

    @SerialName("AU")
    var aU: String? = null

    @SerialName("AT")
    var aT: String? = null
}

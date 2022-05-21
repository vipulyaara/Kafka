package org.rekhta.base.network.model.contentdetail

import kotlinx.serialization.Serializable

@Serializable
class RelatedContentResponseContainer {
    @kotlinx.serialization.SerialName("S")
    var s: Int? = null

    @kotlinx.serialization.SerialName("R")
    var response: RelatedContentResponse? = null

    @kotlinx.serialization.SerialName("T")
    var t: String? = null
}

@Serializable
class RelatedContentResponse {
    @kotlinx.serialization.SerialName("mayBeLike")
    var mayBeLike: List<MayBeLike>? = null

    @kotlinx.serialization.SerialName("nextPrevContent")
    var nextPrevContent: List<NextPrevContent>? = null

    @kotlinx.serialization.SerialName("pluralContent")
    var pluralContent: PluralContent? = null
}

@Serializable
class NextPrevContent {
    @kotlinx.serialization.SerialName("I")
    var i: String? = null

    @kotlinx.serialization.SerialName("CT")
    var cT: String? = null

    @kotlinx.serialization.SerialName("RT")
    var rT: String? = null

    @kotlinx.serialization.SerialName("PN")
    var pN: String? = null

    @kotlinx.serialization.SerialName("CS")
    var cS: String? = null

    @kotlinx.serialization.SerialName("TS")
    var tS: String? = null

    @kotlinx.serialization.SerialName("SI")
    var sI: Int? = null

    @kotlinx.serialization.SerialName("CA")
    var cA: Int? = null

    @kotlinx.serialization.SerialName("RF")
    var rF: Int? = null

    @kotlinx.serialization.SerialName("IN")
    var iN: Boolean? = null

    @kotlinx.serialization.SerialName("PT")
    var pT: String? = null

    @kotlinx.serialization.SerialName("NT")
    var nT: String? = null
}

@Serializable
class PluralContent {
    @kotlinx.serialization.SerialName("PN")
    var pN: String? = null

    @kotlinx.serialization.SerialName("CS")
    var cS: String? = null
}

@Serializable
class MayBeLike {
    @kotlinx.serialization.SerialName("CI")
    var cI: String? = null

    @kotlinx.serialization.SerialName("CT")
    var cT: String? = null

    @kotlinx.serialization.SerialName("PN")
    var pN: String? = null

    @kotlinx.serialization.SerialName("RT")
    var rT: String? = null

    @kotlinx.serialization.SerialName("PS")
    var pS: String? = null

    @kotlinx.serialization.SerialName("CS")
    var cS: String? = null

    @kotlinx.serialization.SerialName("IN")
    var iN: Boolean? = null

    @kotlinx.serialization.SerialName("IU")
    var iU: String? = null

    @kotlinx.serialization.SerialName("DS")
    var dS: String? = null

    @kotlinx.serialization.SerialName("CA")
    var cA: Int? = null

    @kotlinx.serialization.SerialName("RF")
    var rF: Int? = null

    @kotlinx.serialization.SerialName("TF")
    var tF: String? = null

    @kotlinx.serialization.SerialName("TS")
    var tS: String? = null

    @kotlinx.serialization.SerialName("SI")
    var sI: Int? = null
}

package org.rekhta.base.network.model.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.ImageProvider
import org.rekhta.base.network.model.Language

@Serializable
class ContentResponseContainer {
    @SerialName("S")
    var s: Int? = null

    @SerialName("Me")
    var me: String? = null

    @SerialName("Mh")
    var mh: String? = null

    @SerialName("Mu")
    var mu: String? = null

    @SerialName("R")
    var contentResponse: ContentResponse? = null

    @SerialName("T")
    var t: String? = null
}

@Serializable
data class ContentResponse(
    @SerialName("TC") var tC: Int? = null,
    @SerialName("FC") var fC: String? = null, // todo
    @SerialName("SC") var sC: String? = null,
    @SerialName("UE") var uE: String? = null,
    @SerialName("UH") var uH: String? = null,
    @SerialName("UU") var uU: String? = null,
    @SerialName("D") var d: String? = null,
    @SerialName("P") var p: List<P>? = null,
    @SerialName("SI") var sI: List<SI>? = null,
    @SerialName("A") var audio: List<AudioResponse>? = null,
    @SerialName("CD") var renderableContent: List<ContentFromApi>? = null,
    @SerialName("CS") var contentsFromApi: List<ContentFromApi>? = null
)

@Serializable
class T {
    @SerialName("I")
    var i: String? = null

    @SerialName("NE")
    var nE: String? = null

    @SerialName("NH")
    var nH: String? = null

    @SerialName("NU")
    var nU: String? = null

    @SerialName("S")
    var s: String? = null

    @SerialName("T")
    var t: Int? = null

    fun getTagName(currentLanguage: Language): String? {
        return when (currentLanguage) {
            Language.English -> nE
            Language.Hindi -> nH
            Language.Urdu -> nU
        }
    }
}

@Serializable
class SI {
    @SerialName("I")
    var i: String? = null

    @SerialName("NE")
    var nE: String? = null

    @SerialName("NH")
    var nH: String? = null

    @SerialName("NU")
    var nU: String? = null

    @SerialName("DE")
    var dE: String? = null

    @SerialName("DH")
    var dH: String? = null

    @SerialName("DU")
    var dU: String? = null

    @SerialName("S")
    var s: String? = null
}

@Serializable
class P {
    @SerialName("I")
    var i: String? = null

    @SerialName("PI")
    var pI: String? = null

    @SerialName("N")
    var n: String? = null

    @SerialName("CI")
    var cI: String? = null

    @SerialName("IS")
    var iS: Boolean? = null

    @SerialName("S")
    var s: String? = null

    @SerialName("D")
    var d: String? = null

    @SerialName("L")
    var l: Int? = null

    @SerialName("SI")
    var sI: Int? = null

    @SerialName("SU")
    var sU: String? = null

    @SerialName("FC")
    var fC: Int? = null

    @SerialName("SC")
    var sC: Int? = null

    val imageUrl
        get() = "https://www.rekhta.org/images/shayariimages/${s}_medium.jpg"
}

@Serializable
data class AudioResponse(
    @SerialName("AD")
    val aD: String? = null,
    @SerialName("AE")
    val aE: String? = null,
    @SerialName("AH")
    val aH: String? = null,
    @SerialName("AI")
    val aI: String? = null,
    @SerialName("AS")
    val aS: String? = null,
    @SerialName("ASN")
    val aSN: String? = null,
    @SerialName("AU")
    val aU: String? = null,
    @SerialName("CI")
    val cI: String? = null,
    @SerialName("CS")
    val cS: String? = null,
    @SerialName("HA")
    val hA: Boolean? = null,
    @SerialName("HP")
    val hP: Boolean? = null,
    @SerialName("I")
    val i: String? = null,
    @SerialName("NE")
    val nE: String? = null,
    @SerialName("NH")
    val nH: String? = null,
    @SerialName("NU")
    val nU: String? = null,
    @SerialName("PI")
    val pI: String? = null,
    @SerialName("PS")
    val pS: String? = null,
    @SerialName("PSN")
    val pSN: String? = null,
    @SerialName("TE")
    val tE: String? = null,
    @SerialName("TH")
    val tH: String? = null,
    @SerialName("TI")
    val tI: String? = null,
    @SerialName("TU")
    val tU: String? = null,
) {
    val imageUrl: String
        get() = ImageProvider.buildShayarImage(aS.orEmpty())

    val streamUrl: String
        get() = ImageProvider.buildAudioUrl(i.orEmpty())
}

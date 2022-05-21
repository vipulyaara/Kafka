package org.rekhta.base.network.model.poet

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.ImageProvider
import org.rekhta.base.network.model.Language

@Serializable
class PoetResponseContainer {
    @SerialName("S")
    var s: Int? = null

    @SerialName("R")
    var r: PoetResponse? = null

    @SerialName("T")
    var t: String? = null

    @Serializable
    class PoetResponse {
        @SerialName("TC")
        var tC: Int? = null

        @SerialName("P")
        var p: List<Poet>? = null
    }

    @Serializable
    class Poet {
        @SerialName("I")
        var i: String? = null

        @SerialName("NE")
        var nE: String? = null

        @SerialName("NH")
        var nH: String? = null

        @SerialName("NU")
        var nU: String? = null

        @SerialName("P")
        var p: Boolean? = null

        @SerialName("SL")
        var sL: String? = null

        @SerialName("HI")
        var hI: Boolean? = null

        @SerialName("DF")
        var dF: String? = null

        @SerialName("DT")
        var dT: String? = null

        @SerialName("Le")
        var le: String? = null

        @SerialName("Lh")
        var lh: String? = null

        @SerialName("Lu")
        var lu: String? = null

        @SerialName("GC")
        var gC: Int? = null

        @SerialName("NC")
        var nC: Int? = null

        @SerialName("SC")
        var sC: Int? = null

        @SerialName("DE")
        var dE: String? = null

        @SerialName("DH")
        var dH: String? = null

        @SerialName("DU")
        var dU: String? = null

        @SerialName("N")
        var n: Boolean? = null

        @SerialName("SPE")
        var sPE: String? = null

        @SerialName("SPH")
        var sPH: String? = null

        @SerialName("SPU")
        var sPU: String? = null

        @SerialName("CS")
        var cS: List<Count>? = null

        fun getPoetName(currentLanguage: Language) = when (currentLanguage) {
            Language.English -> nE
            Language.Hindi -> nH
            Language.Urdu -> nU
        }

        fun getShortDescription(currentLanguage: Language) = when (currentLanguage) {
            Language.English -> sPE
            Language.Hindi -> sPH
            Language.Urdu -> sPU
        }

        val imageUrl
            get() = ImageProvider.buildShayarImage(sL.orEmpty())
    }

    @Serializable
    class Count {
        @SerialName("I")
        var i: String? = null

        @SerialName("TN")
        var tN: String? = null

        @SerialName("TS")
        var tS: String? = null

        @SerialName("S")
        var s: Int? = null

        @SerialName("C")
        var c: Int? = null

        @SerialName("LT")
        var lT: Int? = null

        @SerialName("CT")
        var cT: Int? = null
    }
}

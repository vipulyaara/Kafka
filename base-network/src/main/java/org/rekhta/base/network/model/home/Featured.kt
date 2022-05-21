package org.rekhta.base.network.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.content.ContentToRender

@Serializable
data class Featured(
    @SerialName("DT") var dT: Int? = null,
    @SerialName("PU") var pU: String? = null,
    @SerialName("PN") var pN: String? = null,
    @SerialName("PI") var pI: String? = null,
    @SerialName("IU") var iU: String? = null,
    @SerialName("T") var t: String? = null,
    @SerialName("FD") var fD: String? = null,
    @SerialName("TD") var tD: String? = null,
    @SerialName("DP") var dP: String? = null,
    @SerialName("R") var description: String? = null,
    @SerialName("DI") var dI: String? = null,
    @SerialName("AT") var aT: String? = null,
    @SerialName("TS") var tS: String? = null,
    @SerialName("CPS") var cPS: String? = null,
    @SerialName("RF") var rF: Int? = null
) {
    val imageUrl: String
        get() = iU.orEmpty()

    val formattedSubtitle: String
        get() = fD.orEmpty() + " - " + tD.orEmpty() + " • " + dP.orEmpty()

    var contentToRender: ContentToRender? = null
}

package org.rekhta.base.network.model.home

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
class TopPoet {
    @SerialName("PI")
    var pI: String? = null

    @SerialName("PN")
    var pN: String? = null

    @SerialName("PS")
    var pS: String? = null

    @SerialName("HI")
    var hI: Boolean? = null

    @SerialName("FD")
    var fD: String? = null

    @SerialName("TD")
    var tD: String? = null

    @SerialName("DP")
    var dP: String? = null

    @SerialName("IU")
    var iU: String? = null

    val imageUrl
        get() = iU.orEmpty()

    val lifeSpan: String
        get() = listOfNotNull(fD, tD).joinToString(" - ")
}

fun String?.sanitizeUrl() = this?.replace("png", "jpg").orEmpty()

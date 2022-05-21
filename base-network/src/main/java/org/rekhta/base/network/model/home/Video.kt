package org.rekhta.base.network.model.home

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.URL_IMAGE_REKHTA_CDN

@Serializable
@Immutable
class Video {
    @SerialName("YI")
    var yI: String? = null

    @SerialName("PN")
    var pN: String? = null

    @SerialName("PU")
    var pU: String? = null

    @SerialName("PI")
    var pI: String? = null

    @SerialName("VT")
    var vT: String? = null

    @SerialName("IU")
    var iU: String? = null

    @SerialName("SE")
    var sE: String? = null

    @SerialName("SH")
    var sH: String? = null

    @SerialName("SU")
    var sU: String? = null

    val imageUrl
        get() = URL_IMAGE_REKHTA_CDN + iU?.sanitizeUrl()
}

package org.rekhta.base.network.model.home

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
class ShayriCollection {
    @SerialName("LI")
    var lI: String? = null

    @SerialName("N")
    var n: String? = null

    @SerialName("TI")
    var tI: String? = null

    @SerialName("IU")
    var iU: String? = null

    @SerialName("PI")
    var pI: String? = null

    @SerialName("T")
    var t: String? = null

    @SerialName("FC")
    var fC: Int? = null

    @SerialName("SC")
    var sC: Int? = null

    val title: String
        get() = n.orEmpty()

    val imageUrl
        get() = iU?.replace("png", "jpg") ?: ""
}

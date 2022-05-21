package org.rekhta.base.network.model.home

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
class SherCollection {
    @SerialName("I")
    var i: String? = null

    @SerialName("N")
    var n: String? = null

    @SerialName("S")
    var s: String? = null

    @SerialName("IU")
    var iU: String? = null

    @SerialName("T")
    var t: String? = null

    @SerialName("FC")
    var fC: Int? = null

    @SerialName("SC")
    var sC: Int? = null

    val imageUrl
        get() = iU.orEmpty()

    val title
        get() = n.orEmpty()
}

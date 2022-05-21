package org.rekhta.base.network.model.home

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class ProseCollection(
    @SerialName("I")
    var i: String? = null,
    @SerialName("N")
    var n: String? = null,
    @SerialName("S")
    var s: String? = null,
    @SerialName("IU")
    var iU: String? = null,
    @SerialName("TI")
    var tI: String? = null,
    @SerialName("TN")
    var tN: String? = null,
    @SerialName("TS")
    var tS: String? = null,
    @SerialName("PI")
    var pI: String? = null,
    @SerialName("T")
    var t: String? = null,
    @SerialName("FC")
    var fC: Int? = null,
    @SerialName("SC")
    var sC: Int? = null
) {

    val imageUrl
        get() = iU.orEmpty()

    val title: String
        get() = n.orEmpty()

    val isPoet
        get() = t == "poet"

    val isCollections
        get() = t == "collections"
}

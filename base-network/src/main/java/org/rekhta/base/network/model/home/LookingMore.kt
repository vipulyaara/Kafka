package org.rekhta.base.network.model.home

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
class LookingMore {
    @SerialName("I")
    var i: String? = null

    @SerialName("T")
    var t: String? = null

    @SerialName("ST")
    var sT: String? = null

    @SerialName("S")
    var s: String? = null

    @SerialName("PI")
    var pI: String? = null

    @SerialName("PN")
    var pN: String? = null

    @SerialName("PS")
    var pS: String? = null

    @SerialName("TI")
    var tI: String? = null

    @SerialName("TN")
    var tN: String? = null

    @SerialName("TS")
    var tS: String? = null

    @SerialName("AC")
    var aC: Int? = null

    @SerialName("VC")
    var vC: Int? = null

    @SerialName("FC")
    var fC: Int? = null
}

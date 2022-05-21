package org.rekhta.base.network.model.home

import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
data class ImageShayri(
    @SerialName("I") var i: String? = null,
    @SerialName("PI") var pI: String? = null,
    @SerialName("PN") var pN: String? = null,
    @SerialName("CI") var cI: String? = null,
    @SerialName("IS") var iS: Boolean? = null,
    @SerialName("TG") var tG: String? = null,
    @SerialName("PS") var pS: String? = null,
    @SerialName("SI") var sI: Int? = null,
    @SerialName("SE") var sE: String? = null,
    @SerialName("SH") var sH: String? = null,
    @SerialName("SU") var sU: String? = null,
    @SerialName("ITG") var iTG: List<ITG>? = null
) {
    val imageUrl
        get() = "https://rekhtacdn.azureedge.net/Images/ShayariImages/${pS}_medium.png"
}

@Serializable
@Immutable
data class ITG(
    @SerialName("TI") var tI: String? = null,
    @SerialName("TN") var tN: String? = null,
    @SerialName("TS") var tS: String? = null,
    @SerialName("CI") var cI: String? = null
)

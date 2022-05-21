package org.rekhta.base.network.model.home

import android.text.Html
import androidx.compose.runtime.Immutable
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Immutable
@Suppress("DEPRECATION")
class DidYouKnow {
    @SerialName("I")
    val i: String? = null

    @SerialName("T")
    val t: String? = null

    @SerialName("R")
    val r: String? = null

    @SerialName("TT")
    val tT: Int? = null

    @SerialName("TN")
    val tN: String? = null

    @SerialName("HI")
    val hI: Boolean? = null

    @SerialName("TI")
    val tI: String? = null

    @SerialName("TS")
    val tS: String? = null

    @SerialName("TH")
    val tH: String? = null

    @SerialName("BG")
    val bG: String? = null

    val formattedText
        get() = Html.fromHtml(r).toString()
}

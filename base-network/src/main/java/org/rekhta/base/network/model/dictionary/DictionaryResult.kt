package org.rekhta.base.network.model.dictionary

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.SearchContent

@Serializable
class DictionaryResult : SearchContent {
    @SerialName("Id")
    var id: String? = null

    @SerialName("Urdu")
    var urdu: String? = null

    @SerialName("Hindi")
    var hindi: String? = null

    @SerialName("English")
    var english: String? = null

    @SerialName("Meaning1_En")
    var meaning1En: String? = null

    @SerialName("Meaning2_En")
    var meaning2En: String? = null

    @SerialName("Meaning3_En")
    var meaning3En: String? = null

    @SerialName("Meaning1_Hi")
    var meaning1Hi: String? = null

    @SerialName("Meaning2_Hi")
    var meaning2Hi: String? = null

    @SerialName("Meaning3_Hi")
    var meaning3Hi: String? = null

    @SerialName("Meaning1_Ur")
    var meaning1Ur: String? = null

    @SerialName("Meaning2_Ur")
    var meaning2Ur: String? = null

    @SerialName("Meaning3_Ur")
    var meaning3Ur: String? = null

    @SerialName("HaveAudio")
    var haveAudio: Boolean? = null

    val meaningsEn
        get() = listOfNotNull(meaning1En, meaning2En, meaning3En).filter { it.isNotEmpty() }
            .joinToString(", ")
}

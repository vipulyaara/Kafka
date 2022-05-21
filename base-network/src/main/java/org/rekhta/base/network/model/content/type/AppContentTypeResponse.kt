package org.rekhta.base.network.model.content.type

import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.Language

@Serializable
data class AppContentTypeResponse(
    @kotlinx.serialization.SerialName("Me")
    val me: String?,
    @kotlinx.serialization.SerialName("Mh")
    val mh: String?,
    @kotlinx.serialization.SerialName("Mu")
    val mu: String?,
    @kotlinx.serialization.SerialName("R")
    val contentTypes: List<AppContentType>,
    @kotlinx.serialization.SerialName("S")
    val s: Int,
    @kotlinx.serialization.SerialName("T")
    val t: String
)

@Serializable
data class AppContentType(
    @kotlinx.serialization.SerialName("CT")
    val cT: Int,
    @kotlinx.serialization.SerialName("DM")
    val dM: String,
    @kotlinx.serialization.SerialName("I")
    val i: String,
    @kotlinx.serialization.SerialName("LT")
    val lT: Int,
    @kotlinx.serialization.SerialName("NE")
    val nE: String,
    @kotlinx.serialization.SerialName("NH")
    val nH: String,
    @kotlinx.serialization.SerialName("NU")
    val nU: String,
    @kotlinx.serialization.SerialName("S")
    val s: Int,
    @kotlinx.serialization.SerialName("SS")
    val sS: String
) {
    fun getNameByLanguage(language: Int) = when (language) {
        Language.English.code -> nE
        Language.Hindi.code -> nH
        else -> nU
    }
}

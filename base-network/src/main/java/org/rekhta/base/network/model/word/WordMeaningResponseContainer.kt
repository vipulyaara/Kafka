package org.rekhta.base.network.model.word

import kotlinx.serialization.Serializable

@Serializable
class WordMeaningResponseContainer {
    @kotlinx.serialization.SerialName("S")
    var s: Int? = null

    @kotlinx.serialization.SerialName("Me")
    var me: String? = null

    @kotlinx.serialization.SerialName("Mh")
    var mh: String? = null

    @kotlinx.serialization.SerialName("Mu")
    var mu: String? = null

    @kotlinx.serialization.SerialName("R")
    var response: WordMeaningResponse? = null

    @kotlinx.serialization.SerialName("T")
    var t: String? = null
}

@Serializable
class WordMeaningResponse {
    @kotlinx.serialization.SerialName("A")
    var a: Boolean? = null

    @kotlinx.serialization.SerialName("I")
    var i: String? = null

    @kotlinx.serialization.SerialName("E")
    var e: String? = null

    @kotlinx.serialization.SerialName("H")
    var h: String? = null

    @kotlinx.serialization.SerialName("U")
    var u: String? = null

    @kotlinx.serialization.SerialName("M1E")
    var m1E: String? = null

    @kotlinx.serialization.SerialName("M2E")
    var m2E: String? = null

    @kotlinx.serialization.SerialName("M3E")
    var m3E: String? = null

    @kotlinx.serialization.SerialName("M1H")
    var m1H: String? = null

    @kotlinx.serialization.SerialName("M2H")
    var m2H: String? = null

    @kotlinx.serialization.SerialName("M3H")
    var m3H: String? = null

    @kotlinx.serialization.SerialName("M1U")
    var m1U: String? = null

    @kotlinx.serialization.SerialName("M2U")
    var m2U: String? = null

    @kotlinx.serialization.SerialName("M3U")
    var m3U: String? = null
}

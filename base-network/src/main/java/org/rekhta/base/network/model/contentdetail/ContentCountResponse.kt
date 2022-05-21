package org.rekhta.base.network.model.contentdetail

import kotlinx.serialization.Serializable

@Serializable
class ContentCountResponseContainer {
    @kotlinx.serialization.SerialName("S")
    var s: Int? = null

    @kotlinx.serialization.SerialName("Me")
    var me: String? = null

    @kotlinx.serialization.SerialName("Mh")
    var mh: String? = null

    @kotlinx.serialization.SerialName("Mu")
    var mu: String? = null

    @kotlinx.serialization.SerialName("R")
    var response: ContentCountResponse? = null

    @kotlinx.serialization.SerialName("T")
    var t: String? = null
}

@Serializable
class ContentCountResponse {
    @kotlinx.serialization.SerialName("I")
    var i: String? = null

    @kotlinx.serialization.SerialName("FC")
    var fC: String? = null

    @kotlinx.serialization.SerialName("SC")
    var sC: String? = null
}

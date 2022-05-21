package org.rekhta.base.network.model.poet.detail

import kotlinx.serialization.Serializable

@Serializable
data class CS(
    @kotlinx.serialization.SerialName("C")
    val c: Int? = null,
    @kotlinx.serialization.SerialName("CT")
    val cT: Int? = null,
    @kotlinx.serialization.SerialName("I")
    val i: String? = null,
    @kotlinx.serialization.SerialName("LT")
    val lT: Int? = null,
    @kotlinx.serialization.SerialName("S")
    val s: Int? = null,
    @kotlinx.serialization.SerialName("TN")
    val tN: String? = null,
    @kotlinx.serialization.SerialName("TS")
    val tS: String? = null
)

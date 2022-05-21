package org.rekhta.base.network.model.poet.detail

import kotlinx.serialization.Serializable

@Serializable
data class UL(
    @kotlinx.serialization.SerialName("DT")
    val dT: String? = null,
    @kotlinx.serialization.SerialName("UT")
    val uT: Int? = null,
    @kotlinx.serialization.SerialName("UU")
    val uU: String? = null
)

package org.rekhta.base.network.model.favorite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FS(
    @SerialName("C")
    val c: Int,
    @SerialName("I")
    val i: String,
    @SerialName("NE")
    val nE: String,
    @SerialName("NH")
    val nH: String,
    @SerialName("NU")
    val nU: String,
    @SerialName("S")
    val s: Int
)

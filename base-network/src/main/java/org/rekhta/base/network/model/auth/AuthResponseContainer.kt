package org.rekhta.base.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuthResponseContainer {
    @SerialName("S")
    var s: Int? = null

    @SerialName("R")
    var response: AuthResponse? = null

    @SerialName("T")
    var t: String? = null
}

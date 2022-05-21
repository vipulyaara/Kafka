package org.rekhta.base.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuthLoginRequestBody(
    @SerialName("Email")
    var email: String? = null,
    @SerialName("Password")
    var password: String? = null,
    @SerialName("Language")
    var language: Int? = null,
    @SerialName("RememberMe")
    var rememberMe: Boolean? = null,
    @SerialName("DeviceParams")
    var deviceParams: String? = null
)

@Serializable
class AuthSignupRequestBody(
    @SerialName("Email")
    var email: String? = null,
    @SerialName("Password")
    var password: String? = null,
    @SerialName("IsRequestedNewsLetter")
    var isRequestedNewsLetter: Boolean? = null,
    @SerialName("DeviceParams")
    var deviceParams: String? = null
)

package org.rekhta.base.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TokenDetails {
    @SerialName("access_token")
    var accessToken: String? = null

    @SerialName("token_type")
    var tokenType: String? = null

    @SerialName("expires_in")
    var expiresIn: Int? = null

    @SerialName("refresh_token")
    var refreshToken: String? = null
}

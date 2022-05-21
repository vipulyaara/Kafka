package org.rekhta.base.network.model.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class AuthResponse {
    @SerialName("DisplayName")
    var displayName: String? = null

    @SerialName("ImageName")
    var imageName: String? = null

    @SerialName("HaveImage")
    var haveImage: Boolean? = null

    @SerialName("Id")
    var id: String? = null

    @SerialName("TokenDetails")
    var tokenDetails: TokenDetails? = null

    @SerialName("SelectedHeaderImage")
    var selectedHeaderImage: Int? = null

    @SerialName("BannerImageName")
    var bannerImageName: String? = null
}

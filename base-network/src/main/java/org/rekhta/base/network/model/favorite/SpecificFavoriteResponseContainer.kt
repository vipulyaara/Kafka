package org.rekhta.base.network.model.favorite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteResponseContainer(
    @SerialName("Me")
    val me: String?,
    @SerialName("Mh")
    val mh: String?,
    @SerialName("Mu")
    val mu: String?,
    @SerialName("R")
    val response: FavoriteResponse?,
    @SerialName("S")
    val s: Int,
    @SerialName("T")
    val t: String
)

@Serializable
data class SpecificFavoriteBody(
    @SerialName("contentIds")
    val contentIds: String
)

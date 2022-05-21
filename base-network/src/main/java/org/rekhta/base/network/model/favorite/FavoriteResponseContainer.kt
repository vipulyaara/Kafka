package org.rekhta.base.network.model.favorite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.content.ContentFromApi

@Serializable
data class SpecificFavoriteResponseContainer(
    @SerialName("Me")
    val me: String?,
    @SerialName("Mh")
    val mh: String?,
    @SerialName("Mu")
    val mu: String?,
    @SerialName("R")
    val response: List<ContentFromApi>,
    @SerialName("S")
    val s: Int,
    @SerialName("T")
    val t: String
)

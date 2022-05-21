package org.rekhta.base.network.model.favorite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.content.ContentFromApi

@Serializable
data class FavoriteResponse(
    @SerialName("FC")
    val favoriteContent: List<ContentFromApi>,
    @SerialName("FS")
    val fS: List<FS>,
    @SerialName("TC")
    val tC: Int
)

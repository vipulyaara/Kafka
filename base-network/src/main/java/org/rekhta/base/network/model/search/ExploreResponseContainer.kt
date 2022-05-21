package org.rekhta.base.network.model.search

import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.content.Tag
import org.rekhta.base.network.model.home.TopPoet

@Serializable
data class ExploreResponseContainer(
    @kotlinx.serialization.SerialName("R")
    val response: ExploreResponse
)

@Serializable
data class ExploreResponse(
    @kotlinx.serialization.SerialName("TopPoets")
    val topPoets: List<TopPoet>,
    @kotlinx.serialization.SerialName("ExploreTags")
    val exploreTags: List<Tag>? = null
)

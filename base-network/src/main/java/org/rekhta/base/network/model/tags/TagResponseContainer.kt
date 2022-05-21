package org.rekhta.base.network.model.tags

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class TagResponseContainer {
    @SerialName("S")
    var s: Int? = null

    @SerialName("Me")
    var me: String? = null

    @SerialName("Mh")
    var mh: String? = null

    @SerialName("Mu")
    var mu: String? = null

    @SerialName("R")
    var response: TagsResponse? = null

    @SerialName("T")
    var t: String? = null
}

@Serializable
class TagsResponse {
    @SerialName("TT")
    var trendingTags: List<Tag>? = null

    @SerialName("TL")
    var allTags: List<TagsList>? = null
}

@Serializable
class TagsList {
    @SerialName("TG")
    var tagGroup: String? = null

    @SerialName("TS")
    var tags: List<Tag>? = null
}

@Serializable
data class Tag(
    @SerialName("I")
    val id: String,
    @SerialName("NE")
    val nameEnglish: String? = null,
    @SerialName("NH")
    val nameHindi: String? = null,
    @SerialName("NU")
    val nameUrdu: String? = null,
    @SerialName("S")
    val s: String? = null,
    @SerialName("T")
    val total: Int = 0
)

package org.rekhta.base.network.model.search

import kotlinx.serialization.Serializable

@Serializable
class Poet {
    @kotlinx.serialization.SerialName("EntityId")
    var entityId: String? = null

    @kotlinx.serialization.SerialName("Name")
    var name: String? = null

    @kotlinx.serialization.SerialName("SEO_Slug")
    var sEOSlug: String? = null

    @kotlinx.serialization.SerialName("ImageUrl")
    var imageUrl: String? = null

    @kotlinx.serialization.SerialName("FromDate")
    var fromDate: String? = null

    @kotlinx.serialization.SerialName("ToDate")
    var toDate: String? = null

    @kotlinx.serialization.SerialName("EntityYearRange")
    var entityYearRange: String? = null

    @kotlinx.serialization.SerialName("IsNew")
    var isNew: Boolean? = null

    @kotlinx.serialization.SerialName("GazalCount")
    var gazalCount: Int? = null

    @kotlinx.serialization.SerialName("NazmCount")
    var nazmCount: Int? = null

    @kotlinx.serialization.SerialName("ProseCount")
    var proseCount: Int? = null

    @kotlinx.serialization.SerialName("PoetryCount")
    var poetryCount: Int? = null

    @kotlinx.serialization.SerialName("ShortUrlIndex")
    var shortUrlIndex: Int? = null
}

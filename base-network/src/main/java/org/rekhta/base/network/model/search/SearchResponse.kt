package org.rekhta.base.network.model.search

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.dictionary.DictionaryResult

@Serializable
class SearchResponse {
    @SerialName("Content")
    var content: List<Content>? = null

    @SerialName("ContentTotal")
    var contentTotal: Int? = null

    @SerialName("GhazalsTotal")
    var ghazalsTotal: Int? = null

    @SerialName("CoupletsTotal")
    var coupletsTotal: Int? = null

    @SerialName("NazmsTotal")
    var nazmsTotal: Int? = null

    @SerialName("Poets")
    var poets: List<Poet>? = null

    @SerialName("PoetsTotal")
    var poetsTotal: Int? = null

    @SerialName("Dictionary")
    var dictionary: List<DictionaryResult>? = null

    //    @kotlinx.serialization.SerialName( "Tags") var tags: List<Any>? = null
//    @kotlinx.serialization.SerialName( "AppPages") var appPages: List<Any>? = null
//    @kotlinx.serialization.SerialName( "Banners") var banners: List<Any>? = null
//    @kotlinx.serialization.SerialName( "T20") var t20: List<Any>? = null
    @SerialName("Message")
    var message: String? = null
}

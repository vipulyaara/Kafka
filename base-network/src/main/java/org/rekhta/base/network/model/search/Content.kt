package org.rekhta.base.network.model.search

import kotlinx.serialization.Serializable

@Serializable
class Content {
    @kotlinx.serialization.SerialName("Id")
    var id: String? = null

    @kotlinx.serialization.SerialName("TypeId")
    var typeId: String? = null

    @kotlinx.serialization.SerialName("Type")
    var type: String? = null

    @kotlinx.serialization.SerialName("Title")
    var title: String? = null

    @kotlinx.serialization.SerialName("Body")
    var body: String? = null

    @kotlinx.serialization.SerialName("ContentSlug")
    var contentSlug: String? = null

    @kotlinx.serialization.SerialName("ContentUrl")
    var contentUrl: String? = null

    @kotlinx.serialization.SerialName("PoetId")
    var poetId: String? = null

    @kotlinx.serialization.SerialName("PoetSlug")
    var poetSlug: String? = null

    @kotlinx.serialization.SerialName("PoetName")
    var poetName: String? = null

    @kotlinx.serialization.SerialName("ImageUrl")
    var imageUrl: String? = null

    @kotlinx.serialization.SerialName("Lang")
    var lang: Int? = null

    @kotlinx.serialization.SerialName("EditorChoice")
    var editorChoice: Boolean? = null

    @kotlinx.serialization.SerialName("PopularChoice")
    var popularChoice: Boolean? = null

    @kotlinx.serialization.SerialName("AudioCount")
    var audioCount: Int? = null

    @kotlinx.serialization.SerialName("VideoCount")
    var videoCount: Int? = null

    @kotlinx.serialization.SerialName("ShortUrlIndex")
    var shortUrlIndex: Int? = null
}

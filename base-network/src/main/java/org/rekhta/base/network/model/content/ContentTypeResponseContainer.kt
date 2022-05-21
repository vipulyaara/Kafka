package org.rekhta.base.network.model.content

import kotlinx.serialization.Serializable
import org.rekhta.base.network.model.Language

@Serializable
class ContentTypeResponseContainer {
    @kotlinx.serialization.SerialName("S")
    var s: Int? = null

    @kotlinx.serialization.SerialName("Me")
    var me: String? = null

    @kotlinx.serialization.SerialName("Mh")
    var mh: String? = null

    @kotlinx.serialization.SerialName("Mu")
    var mu: String? = null

    @kotlinx.serialization.SerialName("R")
    var response: ContentTypeResponse? = null

    @kotlinx.serialization.SerialName("T")
    var t: String? = null

    @Serializable
    class ContentTypeResponse {
        @kotlinx.serialization.SerialName("TargetId")
        var targetId: String? = null

        @kotlinx.serialization.SerialName("TargetName")
        var targetName: String? = null

        @kotlinx.serialization.SerialName("TargetNameEn")
        var targetNameEn: String? = null

        @kotlinx.serialization.SerialName("TargetNameHi")
        var targetNameHi: String? = null

        @kotlinx.serialization.SerialName("TargetNameUr")
        var targetNameUr: String? = null

        @kotlinx.serialization.SerialName("TargetSlug")
        var targetSlug: String? = null

        @kotlinx.serialization.SerialName("HaveBannerImage")
        var haveBannerImage: Boolean? = null

        @kotlinx.serialization.SerialName("TargetType")
        var targetType: String? = null

        @kotlinx.serialization.SerialName("ImageFile")
        var image: String? = null

        @kotlinx.serialization.SerialName("TagList")
        var tagList: List<ContentType>? = null

        fun getName(language: Language) = when (language) {
            Language.English -> targetNameEn
            Language.Hindi -> targetNameHi
            Language.Urdu -> targetNameUr
        }

        @Serializable
        data class ContentType(
            @kotlinx.serialization.SerialName("TypeId")
            var typeId: String,
            @kotlinx.serialization.SerialName("Name_En")
            var nameEn: String,
            @kotlinx.serialization.SerialName("Name_Hi")
            var nameHi: String,
            @kotlinx.serialization.SerialName("Name_Ur")
            var nameUr: String,
            @kotlinx.serialization.SerialName("ContentType")
            var contentType: String,
            @kotlinx.serialization.SerialName("TypeSlug")
            var typeSlug: String
        ) {
            fun getName(language: Language) = when (language) {
                Language.English -> nameEn
                Language.Hindi -> nameHi
                Language.Urdu -> nameUr
            }
        }
    }
}

package org.rekhta.base.network

/**
 * Authored by vipulkumar on 13/12/17.
 *
 * Builds image URLs based on different sources.
 * Rekhta uses different base URLs for different image CDNs.
 */
const val URL_IMAGE_REKHTA_CDN = "https://rekhtacdn.azureedge.net"
const val URL_IMAGE_REKHTA_WORLD_CDN = "https://rekhtaworld.azureedge.net"
const val URL_IMAGE_SHAYAR_CDN = "/images/Shayar/"
const val URL_IMAGE_CARD_CDN = "/Images/Cms/Cards/"

object ImageProvider {
    fun buildShayarImage(slug: String) = "https://rekhta.org/images/shayar/$slug.png"

    fun buildImageUrl(imageUrl: String): String {
        return when {
            imageUrl.contains(URL_IMAGE_SHAYAR_CDN) -> URL_IMAGE_REKHTA_CDN + imageUrl
            else -> "$URL_IMAGE_REKHTA_CDN$URL_IMAGE_SHAYAR_CDN$imageUrl.png"
        }
    }

    fun buildAudioUrl(id: String) =
        "https://rekhtacdn.azureedge.net/Images/SiteImages/Audio/$id.mp3"

    fun buildVideoUrl(imageUrl: String): String {
        return URL_IMAGE_REKHTA_CDN + imageUrl
    }
}

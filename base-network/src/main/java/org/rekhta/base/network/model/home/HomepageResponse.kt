package org.rekhta.base.network.model.home

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomepageResponse(
    @SerialName("Carousels") var carousels: List<Carousel>? = null,
    @SerialName("TopPoets") var topPoets: List<TopPoet>? = null,
    @SerialName("WordOfTheDay") var wordOfTheDay: WordOfTheDay? = null,
    @SerialName("OtherWordOfTheDay") var otherWordOfTheDay: List<WordOfTheDay>? = null,
    @SerialName("Video") var video: Video? = null,
    @SerialName("MoreVideos") var moreVideos: List<Video>? = null,
    @SerialName("Featured") var featured: List<Featured>? = null,
    @SerialName("TodaysTop") var todaysTop: List<TodaysTop>? = null,
    @SerialName("ImgShayari") var imageShayari: List<ImageShayri>? = null,
    @SerialName("ShayariCollection") var shayariCollection: List<ShayriCollection>? = null,
    @SerialName("SherCollection") var sherCollection: List<SherCollection>? = null,
    @SerialName("ProseCollection") var proseCollection: List<ProseCollection>? = null,
    @SerialName("LookingMore") var lookingMore: List<LookingMore>? = null,
    @SerialName("DonateSection") var donateSection: DonateSection? = null,
    @SerialName("DidYouKnow") var didYouKnow: DidYouKnow? = null,
    @SerialName("Blog") var blog: Blog? = null,
    @SerialName("WhatsApp") var shareCard: ShareCard? = null,
)

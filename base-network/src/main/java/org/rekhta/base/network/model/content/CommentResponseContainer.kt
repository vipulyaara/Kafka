package org.rekhta.base.network.model.content


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommentResponseContainer(
    @SerialName("Me")
    val me: String?,
    @SerialName("Mh")
    val mh: String?,
    @SerialName("Mu")
    val mu: String?,
    @SerialName("R")
    val response: Response,
    @SerialName("S")
    val s: Int,
    @SerialName("T")
    val t: String
) {
    @Serializable
    data class Response(
        @SerialName("C")
        val comments: List<Comment>,
        @SerialName("CG")
        val cG: String? = null,
        @SerialName("CT")
        val cT: String? = null,
        @SerialName("TCC")
        val tCC: String? = null,
        @SerialName("TI")
        val tI: String? = null,
        @SerialName("US")
        val uS: String
    ) {
        @Serializable
        data class Comment(
            @SerialName("CD")
            val commentDescription: String,
            @SerialName("CDT")
            val publishedAt: String,
            @SerialName("CI")
            val cI: String,
            @SerialName("IE")
            val iE: Boolean,
            @SerialName("L")
            val language: Int,
            @SerialName("PCI")
            val pCI: String,
            @SerialName("TD")
            val tD: String,
            @SerialName("TH")
            val tH: String,
            @SerialName("TI")
            val tI: String,
            @SerialName("TL")
            val tL: String,
            @SerialName("TR")
            val tR: Int,
            @SerialName("UI")
            val userId: String,
            @SerialName("UN")
            val userName: String
        )
    }
}

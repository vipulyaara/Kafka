@file:Suppress("ConstPropertyName")

package com.kafka.navigation.graph

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class RootScreen(val analyticsKey: String) {
    @Serializable
    data object Home : RootScreen("home_root")

    @Serializable
    data object Search : RootScreen("search_root")

    @Serializable
    data object Library : RootScreen("library_root")

    @Serializable
    data object Profile : RootScreen("profile_root")
}

@Serializable
sealed class Screen {
    @Serializable
    data object Profile : Screen()

    @Serializable
    data object Library : Screen()

    @Serializable
    data object Home : Screen()

    @Serializable
    data object Login : Screen()

    @Serializable
    data object Player : Screen() {
        const val route = "player"
    }

    @Serializable
    data object Feedback : Screen() {
        const val route = "feedback"
    }

    @Serializable
    data class ReportContent(@SerialName("itemId") val itemId: String) : Screen() {
        companion object {
            const val route = "reportContent/{itemId}"
            fun route(itemId: String) = "reportContent/$itemId"
        }
    }

    @Serializable
    data object RecentItems : Screen()

    @Serializable
    data class ItemDetail(
        @SerialName("itemId") val itemId: String,
        @SerialName("origin") val origin: Origin = Origin.Unknown
    ) : Screen() {
        @Serializable
        enum class Origin {
            Carousel, Row, Column, Grid, Recommendation, Unknown;

            companion object {
                fun find(value: String?) = entries.find { it.name == value } ?: Unknown
            }
        }

        data class SharedElementCoverKey(val cover: String?, val origin: Origin)

    }

    @Serializable
    data class AddToBookshelf(
        @SerialName("itemId") val itemId: String,
    ) : Screen() {
        companion object {
            const val route = "add_to_bookshelf/{itemId}"
            fun route(itemId: String) = "add_to_bookshelf/$itemId"
        }
    }

    @Serializable
    data class ItemDescription(@SerialName("itemId") val itemId: String) : Screen() {
        companion object {
            const val route = "item_description/{itemId}"
            fun route(itemId: String) = "item_description/$itemId"
        }
    }

    @Serializable
    data class Search(
        @SerialName("keyword") val keyword: String = ""
    ) : Screen()

    @Serializable
    data class Summary(@SerialName("itemId") val itemId: String) : Screen()

    @Serializable
    data class Web(@SerialName("url") val url: String) : Screen()

    @Serializable
    data class EpubReader(
        @SerialName("itemId") val itemId: String,
        @SerialName("fileId") val fileId: String
    ) : Screen()

    @Serializable
    data class Reviews(
        @SerialName("itemId") val itemId: String
    ) : Screen()

    @Serializable
    data object Back : Screen()
}

val Any.navigationRoute
    get() = this::class.qualifiedName?.removeSuffix(".Companion")

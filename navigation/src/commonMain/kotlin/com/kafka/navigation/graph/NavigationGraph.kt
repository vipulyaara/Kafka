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
    data object RecentItems : Screen()

    @Serializable
    data class ItemDetail(@SerialName("itemId") val itemId: String) : Screen() {
        companion object {
            const val route = "item/{itemId}"
            fun route(itemId: String) = "item/$itemId"
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
    data object Back : Screen()
}

val Any.navigationRoute
    get() = this::class.qualifiedName?.removeSuffix(".Companion")

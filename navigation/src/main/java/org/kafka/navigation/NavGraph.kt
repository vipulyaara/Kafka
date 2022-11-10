package org.kafka.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Home : Screen("home_root")
    object Search : Screen("search_root")
    object PlayerLibrary : Screen("player_library_root")
    object Library : Screen("library_root")
    object Profile : Screen("profile_root")
}

sealed class LeafScreen(private val route: String) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object Home : LeafScreen("home")
    object Search : LeafScreen("search")
    object PlayerLibrary : LeafScreen("player_library")
    object Library : LeafScreen("library")
    object Profile : LeafScreen("profile")

    object WebView : LeafScreen("webview/{url}") {
        fun createRoute(url: String): String = "webview/${url.encodeUrl()}"
    }

    fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())

    object ItemDetail : LeafScreen("item/{itemId}") {
        fun createRoute(itemId: String): String = "item/$itemId"
    }

    object Files : LeafScreen("files/{itemId}") {
        fun createRoute(itemId: String): String = "files/$itemId"
    }

    object Reader : LeafScreen("reader/{itemId}") {
        fun createRoute(arg: String): String = "reader/$arg"
    }
}

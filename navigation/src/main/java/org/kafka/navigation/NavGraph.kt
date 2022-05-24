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

sealed class LeafScreen(val route: String) {
    fun createRoute(root: Screen) = "${root.route}/$route"

    object Home : LeafScreen("home")
    object Search : LeafScreen("search")
    object PlayerLibrary : LeafScreen("player_library")
    object Library : LeafScreen("library")
    object Profile : LeafScreen("profile")
    object Login : LeafScreen("login")

    object LanguageMenu : LeafScreen("language")
    object PlayerBottomSheet : LeafScreen("player_bottom_sheet")

    object Poets : LeafScreen("poets")
    object Settings : LeafScreen("settings")
    object AboutUs : LeafScreen("aboutUs")
    object Blog : LeafScreen("blog")
    object Tags : LeafScreen("tags")

    object WebView : LeafScreen("webview/{url}") {
        fun createRoute(url: String): String = "webview/${url.encodeUrl()}"
    }

    fun String.encodeUrl() = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())

    object PoetDetail : LeafScreen("poet/{poetId}") {
        fun createRoute(poetId: String): String = "poet/$poetId"
    }

    object ContentDetail : LeafScreen("content/{item_id}") {
        fun createRoute(contentId: String): String = "content/$contentId"
    }

    object Reader : LeafScreen("reader/{fileUrl}") {
        fun createRoute(arg: String): String = "reader/$arg"
    }
}

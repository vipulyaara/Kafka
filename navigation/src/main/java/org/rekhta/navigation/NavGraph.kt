package org.rekhta.navigation

import androidx.navigation.NavOptionsBuilder

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
        fun createRoute(url: String): String = "webview/$url"
    }

    object WordMeaning : LeafScreen("word/{word}") {
        fun createRoute(word: String): String = "word/$word"
    }

    object Comments : LeafScreen("comments/{contentId}") {
        fun createRoute(contentId: String): String = "comments/$contentId"
    }

    object PoetDetail : LeafScreen("poet/{poetId}") {
        fun createRoute(poetId: String): String = "poet/$poetId"
    }

    object ContentDetail : LeafScreen("content/{contentId}") {
        fun createRoute(contentId: String): String = "content/$contentId"
    }

    data class ContentEntryParams(
        val contentTypeId: String? = null,
        val contentTypeName: String? = null,
        val poetId: String? = null,
        val targetId: String? = null,
        val title: String? = null
    )

    object ContentEntry : LeafScreen(
        "contentEntry/?contentTypeId={contentTypeId}&contentTypeName={contentTypeName}&poetId={poetId}&targetId={targetId}&title={title}",
    ) {
        fun createRoute(
            contentTypeId: String? = null,
            contentTypeName: String? = null,
            poetId: String? = null,
            targetId: String? = null,
            title: String? = null
        ): String =
            "contentEntry/?contentTypeId=$contentTypeId&contentTypeName=$contentTypeName&poetId=$poetId&targetId=$targetId&title=$title"
    }

    object SpecialContent : LeafScreen(
        "specialContent/?contentTypeId={contentTypeId}&poetId={poetId}&targetId={targetId}&title={title}"
    ) {
        fun createRoute(
            contentTypeId: String? = null,
            poetId: String? = null,
            targetId: String? = null,
            title: String? = null
        ): String =
            "specialContent/?contentTypeId=$contentTypeId&poetId=$poetId&targetId=$targetId&title=$title"
    }
}

fun Navigator.navigateToPoetDetail(id: String) = navigate(LeafScreen.PoetDetail.createRoute(id))

fun Navigator.navigateToContentDetail(id: String, builder: NavOptionsBuilder.() -> Unit = {}) =
    navigate(LeafScreen.ContentDetail.createRoute(id), builder)

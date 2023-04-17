package org.kafka.navigation

import org.kafka.base.debug

sealed class RootScreen(val route: String) {
    object Home : RootScreen("home_root")
    object Search : RootScreen("search_root")
    object Library : RootScreen("library_root")
    object Profile : RootScreen("profile_root")
}

sealed class Screen(
    private val route: String,
) {
    fun createRoute(root: RootScreen) = "${root.route}/$route"

    object Profile : Screen("profile")
    object Library : Screen("library")
    object Home : Screen("home")
    object Login : Screen("login")
    object Player : Screen("player")

    object ItemDetail : Screen("item/{itemId}") {
        fun createRoute(root: RootScreen, itemId: String): String {
            return "${root.route}/item/$itemId"
        }
    }

    object ItemDescription : Screen("item_description/{itemId}") {
        fun createRoute(root: RootScreen, itemId: String): String {
            return "${root.route}/item_description/$itemId"
        }
    }

    object Files : Screen("files/{itemId}") {
        fun createRoute(root: RootScreen, itemId: String): String {
            return "${root.route}/files/$itemId"
        }
    }

    object Reader : Screen("reader/{fileId}") {
        fun createRoute(root: RootScreen, fileId: String): String {
            return "${root.route}/reader/$fileId"
        }
    }

    object Search : Screen("search?keyword={keyword}&filters={filters}") {
        fun createRoute(
            root: RootScreen,
            keyword: String? = null,
            filter: String = SearchFilter.all(),
        ): String {
            val routeKeyword = keyword?.replace("'", " ")
            return "${root.route}/search".let {
                if (routeKeyword != null) "$it?keyword=$routeKeyword&filters=$filter" else it
            }
        }
    }
}


enum class SearchFilter {
    Name, Creator, Subject;

    companion object {
        private fun fromString(value: String): SearchFilter {
            debug { "SearchFilter.fromAlls: $value" }
            return values().first { it.name.lowercase() == value.lowercase() }
        }

        fun from(value: String): List<SearchFilter> {
            debug { "SearchFilter.fromAll: $value" }
            return value.split(",").map { SearchFilter.fromString(it.trim()) }
        }

        fun all(): String {
            return values().toList().joinToString(",") { it.name }
        }
    }
}

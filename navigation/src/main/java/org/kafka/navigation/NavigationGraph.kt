package org.kafka.navigation

import com.kafka.data.model.SearchFilter

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
    object Feedback : Screen("feedback")
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
            filter: String = SearchFilter.allString(),
        ): String {
            val routeKeyword = keyword?.replace("'", " ")
            return "${root.route}/search".let {
                if (routeKeyword != null) "$it?keyword=$routeKeyword&filters=$filter" else it
            }
        }
    }
}

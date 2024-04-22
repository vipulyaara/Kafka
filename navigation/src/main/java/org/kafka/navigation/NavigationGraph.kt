package org.kafka.navigation

import com.kafka.data.model.SearchFilter
import org.kafka.base.extensions.encodeUrl

sealed class RootScreen(val route: String) {
    data object Home : RootScreen("home_root")
    data object Search : RootScreen("search_root")
    data object Library : RootScreen("library_root")
    data object Profile : RootScreen("profile_root")
}

sealed class Screen(private val route: String) {
    fun createRoute(root: RootScreen) = "${root.route}/$route"

    data object Profile : Screen("profile")
    data object Feedback : Screen("feedback")
    data object Library : Screen("library")
    data object Home : Screen("home")
    data object Login : Screen("login")
    data object Player : Screen("player")
    data object RecentItems : Screen("recent_items")

    data object ItemDetail : Screen("item/{itemId}?title={title}&creator={creator}&coverImage={coverImage}") {
        fun createRoute(
            root: RootScreen,
            itemId: String,
            title: String? = "",
            creator: String? = "",
            coverImage: String? = "",
        ): String {
            return "${root.route}/item/$itemId?title=$title&creator=$creator&coverImage=$coverImage"
        }
    }

    data object ItemDescription : Screen("item_description/{itemId}") {
        fun createRoute(root: RootScreen, itemId: String): String {
            return "${root.route}/item_description/$itemId"
        }
    }

    data object Files : Screen("files/{itemId}") {
        fun createRoute(root: RootScreen, itemId: String): String {
            return "${root.route}/files/$itemId"
        }
    }

    data object Reader : Screen("reader/{fileId}") {
        fun createRoute(root: RootScreen, fileId: String): String {
            return "${root.route}/reader/$fileId"
        }
    }

    data object Search : Screen("search?keyword={keyword}&filters={filters}") {
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

    data object Web : Screen("web/{url}") {
        fun createRoute(root: RootScreen, url: String): String {
            return "${root.route}/web/${url.encodeUrl()}"
        }
    }

    data object OnlineReader : Screen("reader_online/{itemId}") {
        fun createRoute(root: RootScreen, itemId: String): String {
            return "${root.route}/reader_online/${itemId}"
        }
    }
}

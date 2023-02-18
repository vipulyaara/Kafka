package org.kafka.navigation

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import org.kafka.navigation.LeafScreen.Login.encodeUrl
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class RootScreen(
    open val route: String,
    open val startScreen: LeafScreen
) {
    object Home : RootScreen("home_root", LeafScreen.Home())
    object Search : RootScreen("search_root", LeafScreen.Search())
    object PlayerLibrary : RootScreen("player_library_root", LeafScreen.PlayerLibrary())
    object Library : RootScreen("library_root", LeafScreen.Library())
    object Profile : RootScreen("profile_root", LeafScreen.Profile())
}

sealed class LeafScreen(
    open val route: String,
    open val rootRoute: String? = null,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) {
    fun createRoute(root: RootScreen? = null) =
        when (val rootPath = root?.route ?: this.rootRoute) {
            is String -> "$rootPath/$route"
            else -> route
        }

    object Login : LeafScreen("login")

    data class Home(
        override val route: String = "home",
        override val rootRoute: String = "home_root"
    ) : LeafScreen(route, rootRoute)

    data class Profile(
        override val route: String = "profile",
        override val rootRoute: String = "home_root"
    ) : LeafScreen(route, rootRoute)

    data class Library(
        override val route: String = "library",
        override val rootRoute: String = "library_root"
    ) : LeafScreen(route, rootRoute)

    data class PlayerLibrary(
        override val route: String = "player_library",
        override val rootRoute: String = "player_library_root"
    ) : LeafScreen(route, rootRoute)

    fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())

    data class Search(
        override val route: String = "search/?keyword={keyword}",
        override val rootRoute: String = "search_root"
    ) : LeafScreen(
        route = route,
        rootRoute = rootRoute,
        arguments = listOf(
            navArgument("keyword") {
                type = NavType.StringType
                nullable = true
            }
        )
    ) {
        companion object {
            fun buildRoute(keyword: String? = "Kaf", root: RootScreen = RootScreen.Search) =
                "${root.route}/search/?keyword=$keyword"
        }
    }

    data class ItemDetail(
        override val route: String = "item/{itemId}",
        override val rootRoute: String = "home_root"
    ) : LeafScreen(
        route = route,
        rootRoute = rootRoute,
        arguments = listOf(
            navArgument("itemId") {
                type = NavType.StringType
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${Config.BASE_URL}item/{itemId}"
            }
        )
    ) {
        companion object {
            fun buildRoute(id: String, root: RootScreen) = "${root.route}/item/$id"
        }
    }

    data class Files(
        override val route: String = "files/{itemId}",
        override val rootRoute: String
    ) : LeafScreen(
        route = route,
        rootRoute = rootRoute,
        arguments = listOf(
            navArgument("itemId") {
                type = NavType.StringType
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${Config.BASE_URL}files/{itemId}"
            }
        )
    ) {
        companion object {
            fun buildRoute(id: String, root: RootScreen) = "${root.route}/files/$id"
        }
    }

    data class WebView(
        override val route: String = "webview/{url}",
        override val rootRoute: String? = null
    ) : LeafScreen(
        route = route,
        rootRoute = rootRoute,
        arguments = listOf(
            navArgument("url") {
                type = NavType.StringType
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${Config.BASE_URL}webview/{url}"
            }
        )
    ) {
        companion object {
            fun buildRoute(url: String, root: RootScreen) = "${root.route}/webview/${url.encodeUrl()}"
        }
    }

    data class Reader(
        override val route: String = "reader/{fileId}",
        override val rootRoute: String
    ) : LeafScreen(
        route = route,
        rootRoute = rootRoute,
        arguments = listOf(
            navArgument("fileId") {
                type = NavType.StringType
            }
        ),
        deepLinks = listOf(
            navDeepLink {
                uriPattern = "${Config.BASE_URL}reader/{fileId}"
            }
        )
    ) {
        companion object {
            fun buildRoute(fileId: String, root: RootScreen) = "${root.route}/reader/$fileId"
        }
    }
}

val ROOT_SCREENS =
    listOf(RootScreen.Home, RootScreen.Search, RootScreen.Library, RootScreen.PlayerLibrary, RootScreen.Profile)

fun NavGraphBuilder.composableScreen(
    screen: LeafScreen,
    root: RootScreen? = null,
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(screen.createRoute(root), screen.arguments, screen.deepLinks, content = content)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.bottomSheetScreen(
    screen: LeafScreen,
    content: @Composable ColumnScope.(NavBackStackEntry) -> Unit
) = bottomSheet(screen.createRoute(), screen.arguments, screen.deepLinks, content)

object Config {
    private const val BASE_HOST = "kafka.xyz"
    const val BASE_URL = "https://$BASE_HOST/"
}

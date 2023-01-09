package org.kafka.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.core.net.toUri
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import org.kafka.navigation.LeafScreen.Home.encodeUrl
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class RootScreen(val route: String, val startScreen: LeafScreen) {
    object Home : RootScreen("home_root", LeafScreen.Home)
    object Search : RootScreen("search_root", LeafScreen.Search)
    object PlayerLibrary : RootScreen("player_library_root", LeafScreen.PlayerLibrary())
    object Library : RootScreen("library_root", LeafScreen.Library)
    object Profile : RootScreen("profile_root", LeafScreen.Profile)
}

sealed class LeafScreen(
    open val route: String,
    open val rootRoute: String? = null,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList(),
) {
    fun createRoute(root: RootScreen? = null) = when (val rootPath = root?.route ?: this.rootRoute) {
        is String -> "$rootPath/$route"
        else -> route
    }

    object Home : LeafScreen("home")
    object Search : LeafScreen("search")
    data class PlayerLibrary(override val route: String = "player_library") : LeafScreen(route)
    object Library : LeafScreen("library")
    object Profile : LeafScreen("profile")

    fun String.encodeUrl(): String = URLEncoder.encode(this, StandardCharsets.UTF_8.toString())

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

            fun buildUri(id: String) = "${Config.BASE_URL}item/$id".toUri()
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
            fun buildUri(id: String) = "${Config.BASE_URL}files/$id".toUri()
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

            fun buildUri(id: String) = "${Config.BASE_URL}webview/$id".toUri()
        }
    }

    data class Reader(
        override val route: String = "reader/{itemId}",
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
                uriPattern = "${Config.BASE_URL}reader/{itemId}"
            }
        )
    ) {
        companion object {
            fun buildRoute(id: String, root: RootScreen) = "${root.route}/reader/$id"
            fun buildUri(id: String) = "${Config.BASE_URL}reader/$id".toUri()
        }
    }
}

val ROOT_SCREENS =
    listOf(RootScreen.Home, RootScreen.Search, RootScreen.Library, RootScreen.PlayerLibrary, RootScreen.Profile)

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.composableScreen(
    screen: LeafScreen,
    content: @Composable AnimatedVisibilityScope.(NavBackStackEntry) -> Unit
) {
    composable(screen.createRoute(), screen.arguments, screen.deepLinks, content = content)
}

@OptIn(ExperimentalMaterialNavigationApi::class)
fun NavGraphBuilder.bottomSheetScreen(
    screen: LeafScreen,
    content: @Composable ColumnScope.(NavBackStackEntry) -> Unit
) =
    bottomSheet(screen.createRoute(), screen.arguments, screen.deepLinks, content)


object Config {
    const val BASE_HOST = "datmusic.xyz"
    const val BASE_URL = "https://$BASE_HOST/"
}

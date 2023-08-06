package org.kafka.navigation.deeplink

import androidx.core.net.toUri

sealed class Navigation {
    data object Homepage : Navigation()
    data class ItemDetail(val itemId: String) : Navigation()
    data class Player(val itemId: String) : Navigation()
    data class Reader(val itemId: String) : Navigation()
    data object Library : Navigation()
    data object LanguageSelection : Navigation()
    data object Profile : Navigation()
    data object Search : Navigation()
}

object DeepLinksNavigation {
    fun findUri(navigation: Navigation) = when (navigation) {
        Navigation.Homepage -> {
            "app.kafka://homepage".toUri()
        }
        is Navigation.ItemDetail -> {
            "app.kafka://item/${navigation.itemId}".toUri()
        }
        is Navigation.Player -> "app.kafka://player/${navigation.itemId}".toUri()
        is Navigation.Reader -> "app.kafka://reader/${navigation.itemId}".toUri()
        is Navigation.Library -> "app.kafka://library".toUri()
        is Navigation.LanguageSelection -> "app.kafka://language".toUri()
        is Navigation.Profile -> "app.kafka://profile".toUri()
        is Navigation.Search -> "app.kafka://search".toUri()
    }
}

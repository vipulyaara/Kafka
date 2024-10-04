package com.kafka.navigation.deeplink

import androidx.core.net.toUri
import com.kafka.navigation.deeplink.Config.BASE_URL

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
            "${BASE_URL}/homepage".toUri()
        }
        is Navigation.ItemDetail -> {
            "${BASE_URL}item/${navigation.itemId}".toUri()
        }
        is Navigation.Player -> "${BASE_URL}/player/${navigation.itemId}".toUri()
        is Navigation.Reader -> "${BASE_URL}/reader/${navigation.itemId}".toUri()
        is Navigation.Library -> "${BASE_URL}/library".toUri()
        is Navigation.LanguageSelection -> "${BASE_URL}/language".toUri()
        is Navigation.Profile -> "${BASE_URL}/profile".toUri()
        is Navigation.Search -> "${BASE_URL}/search".toUri()
    }
}

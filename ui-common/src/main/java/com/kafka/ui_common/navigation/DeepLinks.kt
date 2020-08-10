package com.kafka.ui_common.navigation

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.Navigator

sealed class Navigation {
    object Homepage: Navigation()
    data class ItemDetail(val itemId: String): Navigation()
    data class Player(val itemId: String): Navigation()
    data class Reader(val itemId: String): Navigation()
    object Library : Navigation()
    object LanguageSelection : Navigation()
    object Profile : Navigation()
    object Search : Navigation()
}

fun NavController.navigate(navigation: Navigation, extras: Navigator.Extras? = null) {
    navigate(DeepLinksNavigations.navigate(navigation), null, extras)
}

object DeepLinksNavigations {
    fun navigate(navigation: Navigation) = when (navigation) {
            Navigation.Homepage -> { "app.kafka://homepage".toUri()}
            is Navigation.ItemDetail -> { "app.kafka://item/${navigation.itemId}".toUri() }
            is Navigation.Player -> "app.kafka://player/${navigation.itemId}".toUri()
            is Navigation.Reader -> "app.kafka://reader/${navigation.itemId}".toUri()
            is Navigation.Library -> "app.kafka://library".toUri()
            is Navigation.LanguageSelection -> "app.kafka://language".toUri()
            is Navigation.Profile -> "app.kafka://profile".toUri()
            is Navigation.Search -> "app.kafka://search".toUri()
        }

}
